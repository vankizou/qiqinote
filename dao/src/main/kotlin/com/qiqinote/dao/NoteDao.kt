package com.qiqinote.dao

import com.qiqinote.model.Page
import com.qiqinote.po.Note

/**
 * Created by vanki on 2018/1/23 14:04.
 */
interface NoteDao {
    fun insert(note: Note): Long

    fun updateIdLink(userId: Long, id: Long, idLink: String): Int

    fun updateViewNum(userId: Long, id: Long, viewNum: Long): Int

    fun updateNoteNum(userId: Long, id: Long): Int

    fun updateNoteCountNum(userId: Long, id: Long, noteContentNum: Int): Int

    fun updateById(userId: Long, id: Long, note: Note): Int

    fun updateById(userId: Long, id: Long, fieldValueMap: Map<String, Any?>): Int

    fun deleteById(userId: Long, id: Long): Int

    fun countByParentId(parentId: Long): Int

    fun countNoteHasContent(loginUserId: Long?, userId: Long?): Int

    fun getByIdOrIdLink(id: Long?, idLink: String?): Note?

    fun pageOfCondition(loginUserId: Long?, userId: Long?, parentId: Long?, orderBy: String?, titleLike: String?,
                        onlyNote: Boolean?, totalRow: Int?, currPage: Int, pageSize: Int, navNum: Int): Page<Note>
}