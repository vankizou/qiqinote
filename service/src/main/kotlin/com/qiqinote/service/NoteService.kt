package com.qiqinote.service

import com.qiqinote.dao.NoteDao
import com.qiqinote.model.Page
import com.qiqinote.po.Note
import com.qiqinote.po.NoteDetail
import com.qiqinote.vo.NoteTreeVO
import com.qiqinote.vo.NoteTreeVOAndTotalNote
import com.qiqinote.vo.NoteViewVO
import com.qiqinote.vo.ResultVO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/3/1 18:40.
 */
interface NoteService : BaseService<NoteDao> {
    fun add(loginUserId: Long, note: Note, noteDetailList: List<NoteDetail>?): ResultVO<Long>

    fun updateById(loginUserId: Long, note: Note, noteDetailList: List<NoteDetail>?): ResultVO<Int>

    fun deleteById(loginUserId: Long, id: Long): ResultVO<Int>

    fun getByIdOrIdLink(id: Long?, idLink: String? = null): Note?

    fun getNoteVOByIdOrIdLink(loginUserId: Long?, id: Long?, idLink: String?, password: String?, request: HttpServletRequest? = null, response: HttpServletResponse? = null): NoteViewVO?

    fun listOfNoteTreeVO(loginUserId: Long?, userId: Long, parentId: Long?, deep: Int = 0): MutableList<NoteTreeVO>

    fun listOfNoteTreeVOByTitleLike(loginUserId: Long, titleLike: String): NoteTreeVOAndTotalNote

    fun page(loginUserId: Long?, userId: Long?, parentId: Long?, titleLike: String?, orderBy: String?,
             onlyNote: Boolean?, totalRow: Int?, currPage: Int, pageSize: Int, navNum: Int = 10): Page<Note>

    fun isNoteOpenedInRedis(userId: Long, noteId: Long): Boolean

    fun closeNoteInRedis(userId: Long, noteId: Long)

    fun openNoteInRedis(userId: Long, noteId: Long)

    fun countNoteHasContent(loginUserId: Long?, userId: Long?): Int
}