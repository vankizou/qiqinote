package com.qiqinote.dao.impl

import com.qiqinote.dao.NoteDetailDao
import com.qiqinote.po.NoteDetail
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
 * Created by vanki on 2018/3/20 17:40.
 */
@Repository
class NoteDetailDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : NoteDetailDao {
    private val rowMapper = BeanPropertyRowMapper(NoteDetail::class.java)

    override fun insert(noteDetail: NoteDetail): Long {
        if (noteDetail.userId == null) {
            return 0
        }
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["content"] = noteDetail.content
        paramMap["create_datetime"] = Date()
        paramMap["note_id"] = noteDetail.noteId
        paramMap["sequence"] = noteDetail.sequence
        paramMap["type"] = noteDetail.type
        paramMap["user_id"] = noteDetail.userId

        val sql = NamedSQLUtil.getInsertSQL(NoteDetail::class, paramMap)
        val key = GeneratedKeyHolder()
        val status = this.namedParameterJdbcTemplate.update(sql, MapSqlParameterSource(paramMap), key)
        if (status > 0) {
            return key.key?.toLong() ?: 0
        }
        return 0
    }

    override fun updateByUserIdAndId(noteDetail: NoteDetail): Long {
        if (noteDetail.id == null || noteDetail.userId == null) {
            return 0
        }
        val paramMap = LinkedHashMap<String, Any?>()
        noteDetail.content?.let { paramMap.put("content", it) }
        noteDetail.noteId?.let { paramMap.put("note_id", it) }
        noteDetail.sequence?.let { paramMap.put("sequence", it) }
        noteDetail.type?.let { paramMap.put("type", it) }

        paramMap["user_id"] = noteDetail.userId
        paramMap["id"] = noteDetail.id

        val sql = NamedSQLUtil.getUpdateSQL(NoteDetail::class, paramMap, paramMap.size - 3)
        val status = this.namedParameterJdbcTemplate.update(sql, paramMap)
        if (status > 0) {
            return noteDetail.id!!
        }
        return 0
    }

    override fun deleteById(userId: Long, id: Long): Int {
        val paramMap = mapOf<String, Any?>("user_id" to userId, "id" to id)
        return this.namedParameterJdbcTemplate
                .update(NamedSQLUtil.getDeleteSQL(NoteDetail::class, paramMap), paramMap)
    }

    override fun deleteByNoteId(userId: Long, noteId: Long, excludeIdList: MutableList<Long>?): Int {
        val paramMap = mapOf<String, Any?>("user_id" to userId, "note_id" to noteId)
        var sql = NamedSQLUtil.getDeleteSQL(NoteDetail::class, paramMap)

        if (excludeIdList != null && excludeIdList.isNotEmpty()) {
            val idStr = excludeIdList.toString()
            sql += " AND id NOT IN(" + idStr.substring(1, idStr.length - 1) + ")"
        }
        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun getById(id: Long): NoteDetail? {
        val paramMap = mapOf<String, Any?>("id" to id)
        val list = this.namedParameterJdbcTemplate.query(NamedSQLUtil.getSelectSQL(NoteDetail::class, paramMap), paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun listByNoteId(noteId: Long): MutableList<NoteDetail> {
        val paramMap = mapOf<String, Any?>("note_id" to noteId)
        return this.namedParameterJdbcTemplate.query(NamedSQLUtil.getSelectSQL(NoteDetail::class, paramMap), paramMap, rowMapper)
    }
}