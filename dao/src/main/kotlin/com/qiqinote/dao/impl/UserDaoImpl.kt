package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.UserDao
import com.qiqinote.po.User
import com.qiqinote.util.StringUtil
import com.qiqinote.util.sql.NamedSQLUtil
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by vanki on 2018/3/29 14:55.
 */
@Repository
class UserDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : UserDao {
    private val rowMapper = BeanPropertyRowMapper(User::class.java)

    override fun insert(user: User): Long {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["avatar_id"] = user.avatarId
        paramMap["name"] = user.name
        paramMap["alias"] = if (StringUtil.isBlank(user.alias)) user.name else user.alias
        paramMap["password"] = user.password
        paramMap["gender"] = user.gender
        paramMap["status"] = user.status
        paramMap["motto"] = user.motto
        paramMap["phone"] = user.phone
        paramMap["email"] = user.email
        paramMap["qq"] = user.qq
        paramMap["weixin"] = user.weixin
        paramMap["weibo"] = user.weibo
        paramMap["birthday"] = user.birthday
        paramMap["register_origin"] = user.registerOrigin
        paramMap["register_ip"] = user.registerIp
        paramMap["description"] = user.description
        paramMap["create_datetime"] = Date()
        paramMap["is_del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getInsertSQL(User::class, paramMap)
        val key = GeneratedKeyHolder()
        val status = this.namedParameterJdbcTemplate.update(sql, MapSqlParameterSource(paramMap), key)
        if (status > 0) {
            return key.key?.toLong() ?: 0
        }
        return 0
    }

    override fun updateById(id: Long, user: User): Int {
        if (StringUtil.isBlank(user.name)) user.name = null
        if (StringUtil.isBlank(user.alias)) user.alias = null

        val paramMap = LinkedHashMap<String, Any?>()
        user.avatarId?.let { paramMap["avatar_id"] = it }
        user.name?.let { paramMap["name"] = it }
        user.alias?.let { paramMap["alias"] = it }
        user.password?.let { paramMap["password"] = it }
        user.gender?.let { paramMap["gender"] = it }
        user.status?.let { paramMap["status"] = it }
        user.motto?.let { paramMap["motto"] = it }
        user.phone?.let { paramMap["phone"] = it }
        user.email?.let { paramMap["email"] = it }
        user.qq?.let { paramMap["qq"] = it }
        user.weixin?.let { paramMap["weixin"] = it }
        user.weibo?.let { paramMap["weibo"] = it }
        user.birthday?.let { paramMap["birthday"] = it }
        user.description?.let { paramMap["description"] = it }

        paramMap["id"] = id
        paramMap["is_del"] = DBConst.falseVal

        return this.namedParameterJdbcTemplate.update(NamedSQLUtil.getUpdateSQL(User::class, paramMap, paramMap.size - 1 - 2), paramMap)
    }

    override fun countByName(name: String): Int {
        val paramMap = mapOf("name" to name, "status" to DBConst.User.statusOpen, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(User::class, "COUNT(*)", paramMap)
        return this.namedParameterJdbcTemplate.queryForObject(sql, paramMap, Int::class.java) ?: 0
    }

    override fun getById(id: Long): User? {
        val paramMap = mutableMapOf("id" to id, "status" to DBConst.User.statusOpen, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(User::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun getByName(name: String): User? {
        val paramMap = mapOf("name" to name, "status" to DBConst.User.statusOpen, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(User::class, paramMap)
        val user = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (user.isEmpty()) null else user[0]
    }
}