package com.qiqinote.util.sql

import com.qiqinote.po.Comment
import com.qiqinote.po.SecurityQuestion
import com.qiqinote.po.User
import org.apache.commons.lang3.StringUtils
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Created by vanki on 2018/3/12 16:58.
 */
object NamedSQLUtil {
    fun <T : Any> getDBField(cls: KClass<T>): String {
        val fs = cls.java.declaredFields
        if (fs.size == 0) return ""

        val sqlField = StringBuilder(fs.size * 10)
        fs.forEach { sqlField.append(",").append(fieldToDBField(it.name)) }
        return sqlField.substring(1)
    }

    fun getInsertSQL(cls: KClass<*>, paramMap: Map<String, Any?>) = getInsertSQL(getTableName(cls), paramMap)

    fun getInsertSQL(tableName: String, paramMap: Map<String, Any?>): String {
        if (paramMap.isEmpty()) {
            return ""
        }

        val size = paramMap.size
        val fieldBuilder = StringBuilder(size * 10)
        val valueBuilder = StringBuilder(size * 12)

        for (key in paramMap.keys) {
            fieldBuilder.append(",").append(key)
            valueBuilder.append(",:").append(key)
        }
        return buildInsertSql(tableName, fieldBuilder.substring(1), valueBuilder.substring(1))
    }

    private fun <T : Any> getInsertOneInfo(po: T) = getInsertOneInfoCommon(po, null, DBType.NOT_NULL)

    private fun <T : Any> getInsertOneInfo(tableName: String, po: T) = getInsertOneInfoCommon(po, tableName, DBType.NOT_NULL)


    fun getUpdateSQL(cls: KClass<*>, paramMap: LinkedHashMap<String, Any?>, lastFieldIndex: Int) = getUpdateSQL(getTableName(cls), paramMap, lastFieldIndex)

    /**
     * 获取单表更新SQL语句，WHERE条件统一为 AND 连接（where 条件最少有一个）
     * @param tableName      表名
     * @param paramMap       需要更新值的字段 + 条件字段，key须与表中字段对应
     * @param lastFieldIndex 最后一个<需要要更新值字段>的索引
     *
     * @return
     */
    fun getUpdateSQL(tableName: String, paramMap: LinkedHashMap<String, Any?>, lastFieldIndex: Int): String {
        if (paramMap.isEmpty() || lastFieldIndex < 0 || lastFieldIndex >= paramMap.size - 1) {
            return ""
        }

        val sqlField = StringBuilder(128)
        val sqlCondition = StringBuilder(128)

        for ((indexTemp, key) in paramMap.keys.withIndex()) {
            if (indexTemp <= lastFieldIndex) {
                sqlField.append(",").append(key).append("=:").append(key)
            } else {
                sqlCondition.append(" AND ").append(key).append("=:").append(key)
            }
        }
        val sql = StringBuilder(sqlField.length + sqlCondition.length + 64)
        sql.append("UPDATE ").append(tableName)
        sql.append(" SET ")
        sql.append(sqlField.substring(1))

        if (sqlCondition.isNotEmpty()) {
            sql.append(" WHERE")
            sql.append(sqlCondition.substring(4))
        }
        return sql.toString()
    }

    fun getUpdateSQLWithoutCondition(cls: KClass<*>, paramMap: Map<String, Any?>) = NamedSQLUtil.getUpdateSQLWithoutCondition(getTableName(cls), paramMap)

    fun getUpdateSQLWithoutCondition(tableName: String, paramMap: Map<String, Any?>): String {
        if (paramMap.isEmpty()) {
            return ""
        }

        val sqlField = StringBuilder(128)

        paramMap.keys.forEach {
            sqlField.append(",").append(it).append("=:").append(it)
        }
        val sql = StringBuilder(sqlField.length + 64)
        sql.append("UPDATE ").append(tableName)
        sql.append(" SET ")
        sql.append(sqlField.substring(1))
        return sql.toString()
    }

    fun getDeleteSQL(cls: KClass<*>, conditionMap: Map<String, Any?>) = getDeleteSQL(getTableName(cls), conditionMap)

    fun getDeleteSQL(tableName: String, conditionMap: Map<String, Any?>): String {
        if (conditionMap.isEmpty()) return ""

        val sql = StringBuilder(256)
        sql.append("DELETE FROM ").append(tableName).append(" WHERE ")
        sql.append(getAndCondition(conditionMap))
        return sql.toString()
    }

    fun getSelectSQL(tableName: String, fieldInfo: String, conditionMap: Map<String, Any?>?) = getSelectSQLCommon(tableName, fieldInfo, conditionMap)

    fun getSelectSQL(cls: KClass<*>, conditionMap: Map<String, Any?>?) = getSelectSQLCommon(getTableName(cls), getDBField(cls), conditionMap)

    fun getSelectSQL(tableNameCls: KClass<*>, fieldInfo: String, conditionMap: Map<String, Any?>?) = getSelectSQLCommon(getTableName(tableNameCls), fieldInfo, conditionMap)

    private fun getSelectSQLCommon(tableName: String, fieldInfo: String, conditionMap: Map<String, Any?>?): String {
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(fieldInfo)) {
            return ""
        }
        val mapSize = conditionMap?.size ?: 0
        val sql = StringBuilder(64 + fieldInfo.length + mapSize * 10)
        sql.append("SELECT ").append(fieldInfo).append(" FROM ").append(tableName)
        if (conditionMap != null) {
            sql.append(" WHERE ").append(getAndCondition(conditionMap))
        }
        return sql.toString()
    }

    /**
     * 用AND连接条件
     * @param conditionMap
     *
     * @return
     */
    fun getAndCondition(conditionMap: Map<String, Any?>): String {
        if (conditionMap.isEmpty()) return ""

        val sqlCondition = StringBuffer(128)

        for (key in conditionMap.keys) {
            sqlCondition.append(" AND ").append(key).append("=:").append(key)
        }
        return sqlCondition.substring(5)
    }

    private fun <T : Any> getInsertOneInfoCommon(po: T, tableName: String?, dbType: DBType): NamedSQLInfo? {
        val cls = po::class
        val fs = cls.memberProperties
        val size = fs.size
        if (size == 0) return null
        val tableNameTmp = tableName ?: fieldToDBField(cls.simpleName!!)

        val paramMap = LinkedHashMap<String, Any?>(size)

        val fieldBuilder = StringBuilder(size * 10)
        val valueBuilder = StringBuilder(size * 12)

        var fieldNameTmp: String?
        fs.forEach {
            fieldNameTmp = fieldToDBField(it.name)
            if (dbType == DBType.ALL ||
                    (dbType == DBType.NOT_NULL && it.call(po) != null)) {
                paramMap.put(fieldNameTmp!!, it.call(po))
                fieldBuilder.append(",").append(fieldNameTmp)
                valueBuilder.append(",:").append(fieldNameTmp)
            }
        }
        val sql = buildInsertSql(tableNameTmp, fieldBuilder.substring(1), valueBuilder.substring(1))
        return NamedSQLInfo(sql, paramMap)
    }

    private fun buildInsertSql(tableName: String, fieldSql: String, valueSql: String): String {
        val sqlBuilder = StringBuilder(64 + fieldSql.length + valueSql.length)
        sqlBuilder.append("INSERT INTO ").append(tableName)
        sqlBuilder.append("(").append(fieldSql).append(")")
        sqlBuilder.append(" VALUES(").append(valueSql).append(")")
        return sqlBuilder.toString()
    }

    private fun fieldToDBField(fieldName: String, delim: Char = '_'): String {
        val builder = StringBuilder(fieldName.length + 4)
        fieldName.toCharArray().forEachIndexed { i, c ->
            if (i == 0) {
                builder.append(c.toLowerCase())
            } else {
                if (c.isUpperCase()) {
                    builder.append(delim).append(c.toLowerCase())
                } else {
                    builder.append(c)
                }
            }
        }
        return builder.toString()
    }

    private fun getTableName(cls: KClass<*>) = fieldToDBField(cls.simpleName!!)

    private enum class DBType {
        ALL,
        NOT_NULL
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val user = User()
        user.id = 100
        user.email = "xxxxxx"

        val map = LinkedHashMap<String, Any?>()
        map.put("aaa", "xxcv")
        map.put("bbb", 0)
        map.put("ccc", "xxcv")

        println("getAndCondition: \n" + getAndCondition(map))

        println()
        println("getDBField: \n" + getDBField(User::class))

        println()
        println("getDeleteSQL: \n" + getDeleteSQL("user", map))

        println()
//        println("getInsertOneInfo: \n" + getInsertOneInfo(user))

        println()
//        println("getInsertOneInfo(, , ): \n" + getInsertOneInfo("table_name", user))

        println()
        println("getInsertSQL: \n" + getInsertSQL("table_name", map))

        println()
        println("getUpdateSQL: \n" + getUpdateSQL("table_name", map, 0))

        println()
        println("getUpdateSQLWithoutCondition: \n" + getUpdateSQLWithoutCondition("table_name", map))

        println()
        println("getSelectSQL: \n" + getSelectSQL("table_name", "*", map))

        println()
        println("getSelectSQL: \n" + getSelectSQL(User::class, map))

        println()
        println("getSelectSQL: \n" + getSelectSQL(User::class, "*", map))


        println()
        println()
        printFieldMap(Comment::class)
    }

    private fun <T : Any> printFieldMap(cls: KClass<T>) {
        val props = cls.java.declaredFields
        var clsName = cls.simpleName?.substring(0, 1)?.toLowerCase() + cls.simpleName?.substring(1)

        println("val paramMap = mutableMapOf<String, Any?>()")
        props.forEach({
                        println("""paramMap["${fieldToDBField(it.name)}"] = $clsName.${it.name}""")
//            println("""$clsName.${it.name}?.let{paramMap["${fieldToDBField(it.name)}"] = it}""")
        })
    }
}