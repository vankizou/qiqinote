package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.FindPwdQuestionDao
import com.qiqinote.po.FindPwdQuestion
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
class FindPwdQuestionDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : FindPwdQuestionDao {
    private val rowMapper = BeanPropertyRowMapper(FindPwdQuestion::class.java)

    override fun upsert(question: FindPwdQuestion): Int {
        if (question.userId == null || StringUtil.isBlank(question.question) || StringUtil.isBlank(question.answer)) return 0

        if (question.id == null) {
            val paramMap = mutableMapOf<String, Any?>()
            question.id?.let { paramMap["id"] = it }
            question.userId?.let { paramMap["user_id"] = it }
            question.question?.let { paramMap["question"] = it }
            question.answer?.let { paramMap["answer"] = it }

            question.createDatetime = Date()
            question.isDel = DBConst.falseVal
            val sql = NamedSQLUtil.getInsertSQL(FindPwdQuestion::class, paramMap)
            return this.namedParameterJdbcTemplate.update(sql, paramMap)
        } else {
            val paramMap = LinkedHashMap<String, Any?>()
            question.question?.let { paramMap["question"] = it }
            question.answer?.let { paramMap["answer"] = it }
            question.createDatetime = Date()

            question.id?.let { paramMap["id"] = it }
            question.userId?.let { paramMap["user_id"] = it }
            val sql = NamedSQLUtil.getUpdateSQL(FindPwdQuestion::class, paramMap, paramMap.size - 1 - 2)
            return this.namedParameterJdbcTemplate.update(sql, paramMap)
        }
    }

    override fun deleteOne(id: Long, userId: Long): Int {
        val paramMap = LinkedHashMap<String, Any?>(4)
        paramMap["is_del"] = DBConst.trueVal

        paramMap["id"] = id
        paramMap["user_id"] = userId

        val sql = NamedSQLUtil.getUpdateSQL(FindPwdQuestion::class, paramMap, paramMap.size - 1 - 2)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun getOne(id: Long, userId: Long): FindPwdQuestion? {
        val paramMap = mapOf("id" to id, "user_id" to userId, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(FindPwdQuestion::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun list(userId: Long): MutableList<FindPwdQuestion> {
        val paramMap = mapOf("user_id" to userId, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(FindPwdQuestion::class, paramMap)
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