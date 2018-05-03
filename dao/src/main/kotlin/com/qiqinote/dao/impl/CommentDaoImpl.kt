package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.CommentDao
import com.qiqinote.po.Comment
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by vanki on 2018/5/2 18:06.
 */
@Repository
class CommentDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : CommentDao {
    private val rowMapper = BeanPropertyRowMapper(Comment::class.java)

    override fun create(comment: Comment): Long {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["parent_id"] = comment.parentId
        paramMap["path"] = comment.path
        paramMap["type"] = comment.type
        paramMap["target_id"] = comment.targetId
        paramMap["target_user_id"] = comment.targetUserId
        paramMap["from_user_id"] = comment.fromUserId
        paramMap["to_user_id"] = comment.toUserId
        paramMap["content"] = comment.content
        paramMap["sub_num"] = comment.subNum ?: 0
        paramMap["create_datetime"] = Date()
        paramMap["is_del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getInsertSQL(Comment::class, paramMap)
        val key = GeneratedKeyHolder()
        val status = this.namedParameterJdbcTemplate.update(sql, MapSqlParameterSource(paramMap), key)
        return if (status > 0) key.key!!.toLong() else 0
    }

    override fun updateSubNum(id: Long, subNum: Int): Int {
        val subNumTmp = if (subNum < 0) 0 else subNum

        val paramMap = LinkedHashMap<String, Any?>()
        paramMap["sub_num"] = subNumTmp

        paramMap["id"] = id
        paramMap["is_del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getUpdateSQL(Comment::class, paramMap, paramMap.size - 1 - 2)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun delete(id: Long): Int {
        val paramMap = mapOf("is_del" to DBConst.trueVal)

        val sql = NamedSQLUtil.getUpdateSQLWithoutCondition(Comment::class, paramMap)
        return this.namedParameterJdbcTemplate.update(sql + " WHERE is_del=${DBConst.falseVal} AND id=$id", paramMap)
    }

    override fun getById(id: Long): Comment? {
        val paramMap = mapOf("id" to id, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(Comment::class, paramMap)

        val list = this.namedParameterJdbcTemplate.query(sql, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun list(targetId: Long, parentId: Long): MutableList<Comment> {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["target_id"] = targetId
        paramMap["is_del"] = DBConst.falseVal
        paramMap["parent_id"] = parentId

        val sql = NamedSQLUtil.getSelectSQL(Comment::class, paramMap)
        return this.namedParameterJdbcTemplate.query("$sql ORDER BY ID DESC", paramMap, rowMapper)
    }
}