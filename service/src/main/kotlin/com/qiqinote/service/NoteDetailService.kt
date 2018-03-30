package com.qiqinote.service

import com.qiqinote.po.NoteDetail
import com.qiqinote.dao.NoteDao
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/3/1 18:40.
 */
interface NoteDetailService : BaseService<NoteDao> {
    fun upsert(noteDetail: NoteDetail): ResultVO<Long>

    fun deleteById(loginUserId: Long, id: Long): Int

    fun deleteByNoteId(loginUserId: Long, noteId: Long, excludeIdList: MutableList<Long>): Int

    fun getById(id: Long): NoteDetail?

    fun listByNoteId(noteId: Long): MutableList<NoteDetail>
}