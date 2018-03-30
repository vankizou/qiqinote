package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.NoteDao
import com.qiqinote.model.Page
import com.qiqinote.po.Note
import com.qiqinote.util.StringUtil
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
 * Created by vanki on 2018/3/28 17:51.
 */
@Repository
class NoteDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : NoteDao {
    private val rowMapper = BeanPropertyRowMapper(Note::class.java)

    override fun insert(note: Note): Long {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["parent_id"] = note.parentId
        paramMap["path"] = note.path
        paramMap["user_id"] = note.userId
        paramMap["type"] = note.type
        paramMap["note_num"] = note.noteNum ?: 0
        paramMap["note_content_num"] = note.noteContentNum
        paramMap["secret"] = note.secret
        paramMap["password"] = note.password
        paramMap["title"] = note.title
        paramMap["keyword"] = note.keyword
        paramMap["sequence"] = note.sequence
        paramMap["description"] = note.description
        paramMap["view_num"] = 0L
        paramMap["digest"] = note.digest
        paramMap["author"] = note.author
        paramMap["origin_url"] = note.originUrl
        paramMap["status"] = note.status
        paramMap["status_description"] = note.statusDescription
        paramMap["update_datetime"] = Date()
        paramMap["create_datetime"] = Date()
        paramMap["is_del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getInsertSQL(Note::class, paramMap)
        val key = GeneratedKeyHolder()
        val status = this.namedParameterJdbcTemplate.update(sql, MapSqlParameterSource(paramMap), key)
        if (status > 0) {
            return key.key?.toLong() ?: 0
        }
        return 0
    }

    override fun updateViewNum(userId: Long, id: Long, viewNum: Long) = this.updateById(userId, id, mapOf("view_num" to viewNum))

    override fun updateNoteNum(userId: Long, id: Long): Int {
        val count = this.countByParentId(id)
        return this.updateById(userId, id, mapOf("note_num" to if (count < 0) 0 else count))
    }

    override fun updateNoteCountNum(userId: Long, id: Long, noteContentNum: Int): Int {
        return this.updateById(userId, id, mapOf("note_content_num" to if (noteContentNum < 0) 0 else noteContentNum))
    }

    override fun updateById(userId: Long, id: Long, note: Note): Int {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["parent_id"] = note.parentId
        paramMap["path"] = note.path
        paramMap["type"] = note.type
        paramMap["note_num"] = note.noteNum
        paramMap["note_content_num"] = note.noteContentNum
        paramMap["secret"] = note.secret
        paramMap["password"] = note.password
        paramMap["title"] = note.title
        paramMap["keyword"] = note.keyword
        paramMap["sequence"] = note.sequence
        paramMap["description"] = note.description
        paramMap["view_num"] = note.viewNum
        paramMap["digest"] = note.digest
        paramMap["author"] = note.author
        paramMap["origin_url"] = note.originUrl
        paramMap["status"] = note.status
        paramMap["status_description"] = note.statusDescription
        paramMap["update_datetime"] = Date()
        return this.updateById(id, userId, paramMap)
    }

    override fun updateById(userId: Long, id: Long, fieldValueMap: Map<String, Any?>): Int {
        val paramMap = LinkedHashMap<String, Any?>(fieldValueMap.size + 4)
        paramMap.putAll(fieldValueMap)

        paramMap.remove("id")
        paramMap.remove("user_id")
        paramMap.remove("is_del")

        paramMap["id"] = id
        paramMap["user_id"] = userId
        paramMap["is_del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getUpdateSQL(Note::class, paramMap, paramMap.size - 1 - 3)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun deleteSubNotes(userId: Long, path: String): Int {
        val sql = StringBuilder(128)
        sql.append(NamedSQLUtil.getUpdateSQLWithoutCondition(Note::class, mapOf("is_del" to DBConst.trueVal)))
        sql.append(" WHERE ")
        sql.append("user_id=:userId AND path like ':path%' AND is_del=:isDel")

        val paramMap = mapOf("userId" to userId, "path" to path, "isDel" to DBConst.falseVal)
        return this.namedParameterJdbcTemplate.update(sql.toString(), paramMap)
    }

    override fun countByParentId(parentId: Long): Int {
        val paramMap = mapOf("parent_id" to parentId, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(Note::class, "COUNT(*)", paramMap)
        return this.namedParameterJdbcTemplate.queryForObject(sql, paramMap, Int::class.java) ?: 0
    }

    override fun countNoteHasContent(userId: Long): Int {
        val paramMap = mapOf("user_id" to userId, "is_del" to DBConst.falseVal)
        val sql = StringBuilder(64)
        sql.append(NamedSQLUtil.getSelectSQL(Note::class, "COUNT(*)", paramMap))
        sql.append(" AND note_num>0")
        return this.namedParameterJdbcTemplate.queryForObject(sql.toString(), paramMap, Int::class.java) ?: 0
    }

    override fun getById(id: Long): Note? {
        val paramMap = mapOf("id" to id, "is_del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(Note::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun pageOfCondition(loginUserId: Long?, userId: Long?, parentId: Long?, orderBy: String?,
                                 totalRow: Int?, currPage: Int, pageSize: Int, navNum: Int): Page<Note> {
        val conditionSql = StringBuilder(256)
        conditionSql.append(" WHERE ")

        val paramMap = mutableMapOf<String, Any>()
        paramMap["is_del"] = DBConst.falseVal

        parentId?.let {
            paramMap["parent_id"] = it
        }
        userId?.let {
            paramMap["user_id"] = it
        }
        conditionSql.append(NamedSQLUtil.getAndCondition(paramMap))

        val statusList = arrayListOf<Int>()
        val secretList = arrayListOf<Int>()
        /**
         * 首页等
         */
        if (loginUserId == null || userId == null) {
            statusList.add(DBConst.Note.statusPass)
            secretList.add(DBConst.Note.secretOpen)

            if (userId == null) {
                conditionSql.append(" AND note_content_num>0")
            }
        }
        /**
         * 访问单人主页
         */
        var isMine = true
        if (userId != null && userId != loginUserId) {
            secretList.add(DBConst.Note.secretPwd)
            isMine = false
        }
        if (!secretList.isEmpty()) {
            conditionSql.append(" AND secret")
            if (secretList.size == 1) {
                conditionSql.append("=").append(secretList[0])
            } else {
                val secretListStr = secretList.toString()
                conditionSql.append(" IN(").append(secretListStr.substring(1, secretListStr.length - 1)).append(")")
            }
        }
        if (!statusList.isEmpty()) {
            conditionSql.append(" AND status")
            if (statusList.size == 1) {
                conditionSql.append("=").append(statusList[0])
            } else {
                val statusListStr = statusList.toString()
                conditionSql.append(" IN(").append(statusListStr.substring(1, statusListStr.length - 1)).append(")")
            }
        }
        var totalRowDB = totalRow
        if (totalRowDB == null) {
            totalRowDB = this.namedParameterJdbcTemplate
                    .queryForObject(NamedSQLUtil.getSelectSQL(Note::class, "COUNT(*)", null) + conditionSql.toString(),
                            paramMap, Int::class.java) ?: 0
        }
        val resultPage = Page<Note>(currPage, pageSize, totalRowDB)
        if (totalRowDB <= 0) {
            return resultPage
        }

        if (StringUtil.isNotEmpty(orderBy)) {
            conditionSql.append(" ORDER BY ").append(orderBy)
        } else {
            conditionSql.append(" ORDER BY ").append("ID ASC")
        }
        conditionSql.append(" LIMIT ").append(resultPage.startRow).append(",").append(resultPage.pageSize)

        val results = this.namedParameterJdbcTemplate
                .query(NamedSQLUtil.getSelectSQL(Note::class, null) + conditionSql.toString(), paramMap, rowMapper)

        resultPage.data = results

        /**
         * 不是自己过滤部分字段
         */
        if (!isMine) {
            resultPage.data.let {
                it.forEach({
                    it.password = null
                })
            }
        }
        return resultPage
    }
}