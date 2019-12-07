package com.qiqinote.dao.impl

import com.qiqinote.constant.EnvEnum
import com.qiqinote.dao.EnvDao
import com.qiqinote.po.Env
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Created by vanki on 2019/11/30 15:17.
 */
@Repository
class EnvDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : EnvDao {
    private val rowMapper = BeanPropertyRowMapper(Env::class.java)

    override fun getVByK(k: EnvEnum): String? {
        val paramMap = mapOf("k" to k.name)
        val sql = NamedSQLUtil.getSelectSQL(Env::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0].v
    }
}