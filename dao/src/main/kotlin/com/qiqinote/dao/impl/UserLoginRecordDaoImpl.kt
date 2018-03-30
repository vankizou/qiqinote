package com.qiqinote.dao.impl

import com.qiqinote.dao.UserLoginRecordDao
import com.qiqinote.po.UserLoginRecord
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by vanki on 2018/3/21 12:11.
 */
@Repository
class UserLoginRecordDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : UserLoginRecordDao {

    override fun insert(userLoginRecord: UserLoginRecord): Int {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["accept"] = userLoginRecord.accept
        paramMap["accept_encoding"] = userLoginRecord.acceptEncoding
        paramMap["accept_language"] = userLoginRecord.acceptLanguage
        paramMap["character_encoding"] = userLoginRecord.characterEncoding
        paramMap["connection"] = userLoginRecord.connection
        paramMap["ip"] = userLoginRecord.ip
        paramMap["origin"] = userLoginRecord.origin
        paramMap["protocol"] = userLoginRecord.protocol
        paramMap["remote_addr"] = userLoginRecord.remoteAddr
        paramMap["remote_host"] = userLoginRecord.remoteHost
        paramMap["scheme"] = userLoginRecord.scheme
        paramMap["server_name"] = userLoginRecord.serverName
        paramMap["user_agent"] = userLoginRecord.userAgent
        paramMap["user_id"] = userLoginRecord.userId
        paramMap["create_datetime"] = Date()

        val sql = NamedSQLUtil.getInsertSQL(UserLoginRecord::class, paramMap)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }
}