package com.qiqinote.dao

import com.qiqinote.po.NoteDetail

/**
 * Created by vanki on 2018/1/23 14:04.
 */
interface NoteDetailDao {
    fun insert(noteDetail: NoteDetail): Long

    fun updateByUserIdAndId(noteDetail: NoteDetail): Long

    fun deleteById(userId: Long, id: Long): Int

    fun deleteByNoteId(userId: Long, noteId: Long, excludeIdList: MutableList<Long>?): Int

    fun getById(id: Long): NoteDetail?

    fun listByNoteId(noteId: Long): MutableList<NoteDetail>
}