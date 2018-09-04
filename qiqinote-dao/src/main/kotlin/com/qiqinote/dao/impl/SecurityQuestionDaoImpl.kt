package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.SecurityQuestionDao
import com.qiqinote.po.SecurityQuestion
import com.qiqinote.util.StringUtil
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by vanki on 2018/4/18 14:22.
 */
@Repository
class SecurityQuestionDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : SecurityQuestionDao {
    private val rowMapper = BeanPropertyRowMapper(SecurityQuestion::class.java)

    override fun upsert(question: SecurityQuestion): Int {
        if (question.userId == null || StringUtil.isBlank(question.question) || StringUtil.isBlank(question.answer)) return 0

        if (question.id == null) {
            val paramMap = mutableMapOf<String, Any?>()
            question.id?.let { paramMap["id"] = it }
            question.userId?.let { paramMap["user_id"] = it }
            question.question?.let { paramMap["question"] = it }
            question.answer?.let { paramMap["answer"] = it }

            paramMap["create_datetime"] = Date()
            paramMap["del"] = DBConst.falseVal
            val sql = NamedSQLUtil.getInsertSQL(SecurityQuestion::class, paramMap)
            return this.namedParameterJdbcTemplate.update(sql, paramMap)
        } else {
            val paramMap = LinkedHashMap<String, Any?>()
            question.question?.let { paramMap["question"] = it }
            question.answer?.let { paramMap["answer"] = it }
            paramMap["create_datetime"] = Date()

            question.id?.let { paramMap["id"] = it }
            question.userId?.let { paramMap["user_id"] = it }
            val sql = NamedSQLUtil.getUpdateSQL(SecurityQuestion::class, paramMap, paramMap.size - 1 - 2)
            return this.namedParameterJdbcTemplate.update(sql, paramMap)
        }
    }

    override fun deleteOne(id: Long, userId: Long): Int {
        val paramMap = LinkedHashMap<String, Any?>(4)
        paramMap["del"] = DBConst.trueVal

        paramMap["id"] = id
        paramMap["user_id"] = userId

        val sql = NamedSQLUtil.getUpdateSQL(SecurityQuestion::class, paramMap, paramMap.size - 1 - 2)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun getOne(id: Long, userId: Long): SecurityQuestion? {
        val paramMap = mapOf("id" to id, "user_id" to userId, "del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(SecurityQuestion::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun list(userId: Long): MutableList<SecurityQuestion> {
        val paramMap = mapOf("user_id" to userId, "del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(SecurityQuestion::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query("$sql ORDER BY create_datetime DESC", paramMap, rowMapper)
        if (list.size > 3) {
            list.forEachIndexed { index, findPwdQuestion ->
                if (index >= 3 && findPwdQuestion.id != null && findPwdQuestion.userId != null) {
                    this.deleteOne(findPwdQuestion.id!!, findPwdQuestion.userId!!)
                }
            }
        }
        return list
    }
}