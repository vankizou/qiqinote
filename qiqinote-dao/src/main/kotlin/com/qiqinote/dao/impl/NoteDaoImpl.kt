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
        paramMap["note_num2"] = note.noteNum2 ?: 0
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
        paramMap["del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getInsertSQL(Note::class, paramMap)
        val key = GeneratedKeyHolder()
        val status = this.namedParameterJdbcTemplate.update(sql, MapSqlParameterSource(paramMap), key)
        if (status > 0) {
            return key.key?.toLong() ?: 0
        }
        return 0
    }

    override fun updateIdLink(userId: Long, id: Long, idLink: String) = this.updateById(userId, id, mapOf("id_link" to idLink))

    override fun updateViewNum(userId: Long, id: Long, viewNum: Long) = this.updateById(userId, id, mapOf("view_num" to viewNum))

    override fun updateNoteNum(userId: Long, id: Long): Int {
        val count1 = this.countByParentId(id, true)
        val count2 = this.countByParentId(id, false)
        return this.updateById(
                userId,
                id,
                mapOf(
                        "note_num" to if (count1 < 0) 0 else count1,
                        "note_num2" to if (count2 < 0) 0 else count2
                )
        )
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
        paramMap["note_num2"] = note.noteNum2
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
        paramMap.remove("del")

        paramMap["id"] = id
        paramMap["user_id"] = userId
        paramMap["del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getUpdateSQL(Note::class, paramMap, paramMap.size - 1 - 3)
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun updatePath(parentId: Long, parentPath: String) {
        val sql = "select id, parent_id, path from note where parent_id=$parentId"
        val list = this.namedParameterJdbcTemplate.query(sql, rowMapper)
        if (list.isEmpty()) return

        list.forEach {
            val path = if (parentId == DBConst.defaultParentId) DBConst.defaultParentId.toString() else parentPath + DBConst.Note.pathLink + parentId
            val sql2 = "update note set path='$path' where parent_id=$parentId"
            this.namedParameterJdbcTemplate.update(sql2, mutableMapOf<String, Any>())
            updatePath(it.id!!, path)
        }
    }

    override fun deleteById(userId: Long, id: Long): Int {
        val note = this.getByIdOrIdLink(id, null) ?: return 1
        if (note.userId != userId) return 1

        val paramMap = LinkedHashMap<String, Any?>(4)
        paramMap["del"] = DBConst.trueVal

        paramMap["id"] = id
        paramMap["user_id"] = userId

        val status = this.namedParameterJdbcTemplate.update(NamedSQLUtil.getUpdateSQL(Note::class, paramMap, paramMap.size - 1 - 2), paramMap)
        if (status > 0) {
            deleteSubNotes(userId, note.id!!, note.path + DBConst.Note.pathLink + note.id)
            return 1
        }
        return status
    }

    private fun deleteSubNotes(userId: Long, parentId: Long, path: String): Int {
        val paramMap = mutableMapOf<String, Any>("del" to DBConst.trueVal)

        val sql = StringBuilder(128)
        sql.append(NamedSQLUtil.getUpdateSQLWithoutCondition(Note::class, paramMap))
        sql.append(" WHERE ")
        sql.append("user_id=:userId")

        paramMap["userId"] = userId

        val s2 = this.namedParameterJdbcTemplate.update("$sql AND parent_id=$parentId", paramMap)
        val s1 = this.namedParameterJdbcTemplate.update("$sql AND path like '$path\\_%'", paramMap)
        return s1 + s2
    }

    override fun countByParentId(parentId: Long, isAuthor: Boolean): Int {
        val paramMap = mapOf("parent_id" to parentId, "del" to DBConst.falseVal)
        val sql = StringBuilder()
        sql.append(NamedSQLUtil.getSelectSQL(Note::class, "COUNT(distinct id)", paramMap))

        if (!isAuthor) {
            // 别人看的统计数
            this.buildSecretAndStatus(sql, null, null, true)
        }

        return this.namedParameterJdbcTemplate.queryForObject(sql.toString(), paramMap, Int::class.java) ?: 0
    }

    override fun countNoteHasContent(loginUserId: Long?, userId: Long?): Int {
        if (loginUserId == null && userId == null) return 0

        val paramMap = mapOf("user_id" to userId, "del" to DBConst.falseVal)
        val sql = StringBuilder(64)
        sql.append(NamedSQLUtil.getSelectSQL(Note::class, "COUNT(*)", paramMap))
        sql.append(" AND note_content_num>0")
        this.buildSecretAndStatus(sql, loginUserId, userId, true)

        return this.namedParameterJdbcTemplate.queryForObject(sql.toString(), paramMap, Int::class.java) ?: 0
    }

    override fun getByIdOrIdLink(id: Long?, idLink: String?): Note? {
        if ((id == null || id == DBConst.defaultParentId) && idLink == null) return null

        val paramMap = mutableMapOf<String, Any>()
        paramMap["del"] = DBConst.falseVal
        id?.let { paramMap["id"] = id }
        idLink?.let { paramMap["id_link"] = idLink }

        val sql = NamedSQLUtil.getSelectSQL(Note::class, paramMap)
        val list = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun pageOfCondition(loginUserId: Long?, userId: Long?, parentId: Long?, orderBy: String?, titleLike: String?,
                                 isTree: Boolean, totalRow: Int?, currPage: Int, pageSize: Int, navNum: Int): Page<Note> {
        val conditionSql = StringBuilder(256)
        conditionSql.append(" WHERE ")

        val paramMap = mutableMapOf<String, Any>()
        paramMap["del"] = DBConst.falseVal

        parentId?.let {
            paramMap["parent_id"] = it
        }
        userId?.let {
            paramMap["user_id"] = it
        }
        conditionSql.append(NamedSQLUtil.getAndCondition(paramMap))

        titleLike?.let {
            if (StringUtil.isNotEmpty(titleLike.trim())) {
                conditionSql.append(" AND title like '%${titleLike.trim()}%'")
            }
        }

        /**
         * 首页
         */
        if (!isTree || ((loginUserId == null || userId != loginUserId) && userId == null)) {
            conditionSql.append(" AND note_content_num>0")
        }

        this.buildSecretAndStatus(conditionSql, loginUserId, userId, isTree)

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
            conditionSql.append(" ORDER BY ").append("note_num DESC")
        }
        conditionSql.append(" LIMIT ").append(resultPage.startRow).append(",").append(resultPage.pageSize)

        val results = this.namedParameterJdbcTemplate
                .query(NamedSQLUtil.getSelectSQL(Note::class, null) + conditionSql.toString(), paramMap, rowMapper)

        resultPage.data = results

        /**
         * 不是自己过滤部分字段
         */
        if (userId != null && userId != loginUserId) {
            resultPage.data.let { out ->
                out.forEach {
                    it.password = null
                }
            }
        }
        return resultPage
    }

    private fun buildSecretAndStatus(sql: StringBuilder, loginUserId: Long?, userId: Long?, isTree: Boolean) {
        val statusList = arrayListOf<Int>()
        val secretList = arrayListOf<Int>()
        /**
         * 首页等
         */
        if (loginUserId == null || userId != loginUserId) {
            statusList.add(DBConst.Note.statusPass)
            secretList.add(DBConst.Note.secretOpen)
        }
        /**
         * 访问单人主页
         */
        if (((userId == null || loginUserId == null) || userId != loginUserId) && isTree) {
            secretList.add(DBConst.Note.secretPwd)
        }
        if (secretList.isNotEmpty()) {
            sql.append(" AND (")

            val secretSql = StringBuilder(32)
            secretList.forEach {
                secretSql.append(" OR secret=").append(it)
            }
            sql.append(secretSql.substring(4))
            sql.append(")")
        }
        if (statusList.isNotEmpty()) {
            sql.append(" AND (")

            val statusSql = StringBuilder(32)
            statusList.forEach {
                statusSql.append(" OR status=").append(it)
            }
            sql.append(statusSql.substring(4))
            sql.append(")")
        }
    }
}