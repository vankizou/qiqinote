package com.qiqinote.dao.impl

import com.alibaba.fastjson.JSON
import com.qiqinote.dao.ConfigDao
import com.qiqinote.po.Config
import com.qiqinote.po.fieldjson.ConfigNoteTree
import com.qiqinote.util.StringUtil
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by vanki on 2018/9/3 16:47.
 */
@Repository
class ConfigDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : ConfigDao {

    override fun upsert(config: Config): Int {
        if (config.userId == null) {
            return -1
        }

        val old = this.getByUserId(config.userId!!)
        return if (old == null) {
            this.insert(config)
        } else {
            this.updateByUserId(config)
        }
    }

    fun insert(config: Config): Int {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["user_id"] = config.userId
        config.noteTreeConfig?.let { paramMap["note_tree_config"] = JSON.toJSONString(it) }
        paramMap["create_datetime"] = Date()
        paramMap["update_datetime"] = Date()

        val sql = NamedSQLUtil.getInsertSQL(Config::class, paramMap)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    fun updateByUserId(config: Config): Int {
        val paramMap = linkedMapOf<String, Any?>()
        config.noteTreeConfig?.let { paramMap["note_tree_config"] = JSON.toJSONString(it) }
        paramMap["update_datetime"] = Date()

        paramMap["user_id"] = config.userId

        val sql = NamedSQLUtil.getUpdateSQL(Config::class, paramMap, paramMap.size - 1 - 1)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun getByUserId(userId: Long): Config? {
        val paramMap = mapOf("user_id" to userId)
        val sql = NamedSQLUtil.getSelectSQL(Config::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query(sql, paramMap) { rs, _ ->
            val config = Config()
            config.id = rs.getLong("id")
            config.userId = rs.getLong("user_id")

            val noteTreeConfig = rs.getString("note_tree_config")
            if (StringUtil.isNotBlank(noteTreeConfig)) {
                config.noteTreeConfig = JSON.parseObject(noteTreeConfig, ConfigNoteTree::class.java)
            }
            config
        }
        return if (list.isEmpty()) null else list[0]
    }
}