package com.qiqinote.service.impl

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.constant.RedisKeyEnum
import com.qiqinote.constant.ServiceConst
import com.qiqinote.dao.NoteDao
import com.qiqinote.po.Note
import com.qiqinote.po.NoteDetail
import com.qiqinote.service.NoteDetailService
import com.qiqinote.service.NoteService
import com.qiqinote.util.EntityUtil
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.NoteTreeVO
import com.qiqinote.vo.NoteViewVO
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by vanki on 2018/3/1 18:40.
 */
@Service
class NoteServiceImpl @Autowired constructor(
        private val noteDao: NoteDao,
        private val noteDetailService: NoteDetailService,
        private val stringRedisTemplate: StringRedisTemplate
) : NoteService {
    private val maxTitleLen = 200
    private val defaultPwd = ""
    private val pathLink = "_"

    override fun add(loginUserId: Long, note: Note, noteDetailList: List<NoteDetail>?): ResultVO<Long> {
        if (StringUtil.isEmpty(note.title?.trim())) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }
        note.userId = loginUserId
        val noteTitle = note.title!!.trim()

        if (noteTitle.length > maxTitleLen) {
            return ResultVO(CodeEnum.NOTE_TITLE_LEN_ERROR)
        }

        note.type = note.type ?: DBConst.Note.typeMarkdown
        note.sequence = note.sequence ?: DBConst.firstSequence
        note.parentId = note.parentId ?: DBConst.defaultParentId

        if (DBConst.defaultParentId == note.parentId) {
            note.path = DBConst.defaultParentId.toString()
        } else {
            val parent = this.getById(note.parentId!!)

            if (parent == null || note.userId != parent.userId) {
                return ResultVO(CodeEnum.FAIL)
            }
            if (note.secret == null) {
                if (parent.secret == DBConst.Note.secretPwd) {
                    note.secret = DBConst.Note.secretPwd
                    note.password = parent.password
                } else if (parent.secret == DBConst.Note.secretClose) {
                    note.secret = DBConst.Note.secretClose
                }
            }
            note.path = parent.path + pathLink + parent.id
        }

        note.secret = note.secret ?: DBConst.Note.secretOpen

        if (note.secret != DBConst.Note.secretPwd) {
            note.password = null
        } else if (note.password == null) {
            note.password = defaultPwd
        }

        this.setDefaultStatus(note)
        note.noteContentNum = noteDetailList?.size ?: 0

        val id = this.noteDao.insert(note)
        if (id > 0) {
            /**
             * 如果有笔记内容
             */
            if (noteDetailList != null && !noteDetailList.isEmpty()) {
                val countNoteContentReal = upsertNoteDetail(loginUserId, id, noteDetailList)
                if (countNoteContentReal != note.noteContentNum) {
                    this.noteDao.updateNoteCountNum(loginUserId, id, countNoteContentReal)
                }
            }
            /**
             * 更新父节点笔记数量
             */
            if (note.parentId != null && note.parentId != DBConst.defaultParentId) {
                this.updateNoteNum(loginUserId, note.parentId!!)
            }
        }
        return ResultVO(id)
    }

    override fun updateById(loginUserId: Long, note: Note, noteDetailList: List<NoteDetail>?): ResultVO<Int> {
        if (note.id == null) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }
        if (StringUtil.isNotEmpty(note.title) && (note.title?.length!! > ServiceConst.maxNoteTitleLen)) {
            return ResultVO(CodeEnum.NOTE_TITLE_LEN_ERROR)
        }

        val old = this.getById(note.id!!) ?: return ResultVO(CodeEnum.NOT_FOUND)
        if (loginUserId != old.userId) {
            return ResultVO(CodeEnum.FORBIDDEN)
        }
        note.userId = null

        val newParentId = note.parentId ?: DBConst.defaultParentId
        val oldParentId = old.parentId ?: DBConst.defaultParentId

        if (newParentId != DBConst.defaultParentId && newParentId != oldParentId) {
            val parent = this.getById(newParentId) ?: return ResultVO(CodeEnum.NOT_FOUND)
            note.path = parent.path + pathLink + newParentId
        }

        EntityUtil.copyValIfNull(note, old)

        if (note.secret != DBConst.Note.secretPwd && StringUtil.isNotEmpty(note.password)) {
            note.password = null
        }
        if (noteDetailList != null && !noteDetailList.isEmpty()) {
            note.noteContentNum = this.upsertNoteDetail(loginUserId, note.id!!, noteDetailList)
        }
        if (newParentId == DBConst.defaultParentId) {
            note.path = DBConst.defaultParentId.toString()
        }
        val status = this.noteDao.updateById(note.id!!, loginUserId, note)
        if (newParentId != oldParentId) {
            updateNoteNum(loginUserId, oldParentId)
            updateNoteNum(loginUserId, newParentId)
        }
        return ResultVO(status)
    }

    override fun deleteById(loginUserId: Long, id: Long): ResultVO<Int> {
        val old = this.getById(id) ?: return ResultVO()
        if (old.userId != loginUserId) {
            return ResultVO()
        }
        old.isDel = DBConst.trueVal
        this.updateNoteNum(loginUserId, old.parentId ?: DBConst.defaultParentId)
        this.closeNoteInRedis(loginUserId, id)
        this.deleteSubNotes(loginUserId, old.id!!, old.path ?: DBConst.defaultParentId.toString())
        return ResultVO()
    }

    fun deleteSubNotes(loginUserId: Long, parentId: Long, parentPath: String) =
            this.noteDao.deleteSubNotes(loginUserId, "$parentPath$pathLink$parentId")

    override fun getById(id: Long) = this.noteDao.getById(id)

    override fun getNoteVOById(loginUserId: Long?, id: Long, password: String?): NoteViewVO? {
        val note = this.getById(id) ?: return null
        var secret = note.secret ?: DBConst.Note.secretOpen

        /**
         * 拒绝访问
         */
        if (loginUserId != note.userId) {
            if (note.status != DBConst.Note.statusPass || secret == DBConst.Note.secretClose) {
                return null
            }
            if (secret == DBConst.Note.secretPwd && note.password != null && !note.password.equals(password)) {
                val pwdNote = Note()
                pwdNote.id = id
                pwdNote.title = note.title

                val noteVo = NoteViewVO()
                noteVo.needPwd = ServiceConst.trueVal
                noteVo.note = pwdNote
                return noteVo
            }
        }

        val vo = NoteViewVO()
        vo.note = note
        vo.noteDetailList = this.noteDetailService.listByNoteId(note.id!!)

        if (loginUserId != note.userId) {
            this.updateViewNum(note.userId!!, id, (note.viewNum ?: 0) + 1)
        }
        return vo
    }

    override fun listOfNoteTreeVO(loginUserId: Long?, userId: Long, parentId: Long?, deep: Int): MutableList<NoteTreeVO> {
        if (deep > 5) return arrayListOf()
        val h = 10    // 横向找最多打开的节点数
        var hCount = 0

        var isMine = false
        if (loginUserId == userId) {
            isMine = true
        }

        var currPage = 1
        val pageSize = 200

        val flag = true
        var resultList: MutableList<NoteTreeVO>? = null
        var noteListTmp: MutableList<Note>?
        var totalRowTmp: Int? = null
        var voTmp: NoteTreeVO?

        do {
            val page = this.page(loginUserId, userId, parentId, totalRowTmp, currPage, pageSize)
            noteListTmp = page.data
            if (noteListTmp.isEmpty()) break
            if (resultList == null) {
                totalRowTmp = page.totalRow
                resultList = ArrayList(totalRowTmp)
            }

            /**
             * 获取展开的数据
             */
            for (note in noteListTmp) {
                voTmp = NoteTreeVO()

                if (isMine && hCount < h && note.noteNum != null &&
                        note.noteNum != 0 && this.isNoteOpenedInRedis(userId, note.id ?: continue)) {
                    hCount++
                    voTmp.subNoteVOList = this.listOfNoteTreeVO(loginUserId, userId, note.id, deep + 1)
                }
                voTmp.note = note
                resultList.add(voTmp)
            }
            if (noteListTmp.size < pageSize) break
            currPage++
        } while (flag)
        return resultList ?: arrayListOf()
    }

    override fun page(loginUserId: Long?, userId: Long?, parentId: Long?, totalRow: Int?, currPage: Int, pageSize: Int, navNum: Int) = this.noteDao.pageOfCondition(loginUserId, userId, parentId, "note_num DESC, TITLE ASC", totalRow, currPage, pageSize, navNum)

    override fun isNoteOpenedInRedis(userId: Long, noteId: Long): Boolean {
        if (DBConst.defaultParentId == noteId) {
            return true
        }
        return this.stringRedisTemplate.opsForSet()
                .isMember(RedisKeyEnum.sOpenedNoteId_.name + userId, noteId.toString()) ?: false
    }

    override fun closeNoteInRedis(userId: Long, noteId: Long) {
        if (DBConst.defaultParentId == noteId) {
            return
        }
        this.stringRedisTemplate.opsForSet().remove(RedisKeyEnum.sOpenedNoteId_.name + userId, noteId.toString())
    }

    override fun openNoteInRedis(userId: Long, noteId: Long) {
        if (DBConst.defaultParentId == noteId) {
            return
        }
        this.stringRedisTemplate.opsForSet().add(RedisKeyEnum.sOpenedNoteId_.name + userId, noteId.toString())
    }

    override fun countNoteHasContent(userId: Long) = this.noteDao.countNoteHasContent(userId)

    private fun updateViewNum(userId: Long, id: Long, viewNum: Long) = this.noteDao.updateViewNum(userId, id, viewNum)

    private fun updateNoteNum(loginUserId: Long, parentId: Long) {
        if (parentId == DBConst.defaultParentId) return
        this.noteDao.updateNoteNum(loginUserId, parentId)
    }

    private fun upsertNoteDetail(userId: Long, noteId: Long, noteDetailList: List<NoteDetail>): Int {
        val uDetailIdList = ArrayList<Long>()

        for (detail in noteDetailList) {
            uDetailIdList.add(detail.id ?: continue)
        }
        this.noteDetailService.deleteByNoteId(userId, noteId, uDetailIdList)

        var execCount = 0
        var result: ResultVO<Long>
        var sequence = DBConst.firstSequence

        for (detail in noteDetailList) {
            if (detail.content == null) {
                continue
            }

            if ("".equals(detail.content)) {
                this.noteDetailService.deleteById(userId, detail.id ?: continue)
                continue
            }

            detail.noteId = noteId
            detail.userId = userId
            detail.sequence = sequence

            result = this.noteDetailService.upsert(detail)
            if (result.isSuccess()) {
                execCount++
                sequence++
            }
        }
        return execCount
    }

    /**
     * 设置默认审核状态

     * @param note
     */
    private fun setDefaultStatus(note: Note) {
        note.status = DBConst.Note.statusPass
    }
}