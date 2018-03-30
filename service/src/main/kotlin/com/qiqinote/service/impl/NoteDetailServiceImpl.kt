package com.qiqinote.service.impl

import com.qiqinote.constant.CodeEnum
import com.qiqinote.dao.NoteDetailDao
import com.qiqinote.po.NoteDetail
import com.qiqinote.service.NoteDetailService
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/1/24 18:47.
 */
@Service
class NoteDetailServiceImpl @Autowired constructor(
        private val noteDetailDao: NoteDetailDao
) : NoteDetailService {

    override fun upsert(noteDetail: NoteDetail): ResultVO<Long> {
        if (noteDetail.userId == null || noteDetail.noteId == null || StringUtil.isEmpty(noteDetail.content)) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }

        var id = if (noteDetail.id == null) {
            /**
             * 添加
             */
            this.noteDetailDao.insert(noteDetail)
        } else {
            /**
             * 更新
             */
            val old = this.getById(noteDetail.id!!) ?: return ResultVO(CodeEnum.NOT_FOUND)
            if (noteDetail.userId != old.userId) return ResultVO(CodeEnum.FORBIDDEN)
            this.noteDetailDao.updateByUserIdAndId(noteDetail)
        }
        return if (id > 0) ResultVO(id) else ResultVO(CodeEnum.FAIL)
    }

    override fun deleteById(loginUserId: Long, id: Long) = this.noteDetailDao.deleteById(loginUserId, id)

    override fun deleteByNoteId(loginUserId: Long, noteId: Long, excludeIdList: MutableList<Long>): Int = this.noteDetailDao.deleteByNoteId(loginUserId, noteId, excludeIdList)

    override fun getById(id: Long) = this.noteDetailDao.getById(id)

    override fun listByNoteId(noteId: Long) = this.noteDetailDao.listByNoteId(noteId)
}