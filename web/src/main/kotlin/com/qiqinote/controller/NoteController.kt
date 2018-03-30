package com.qiqinote.controller

import com.qiqinote.constant.*
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.model.Page
import com.qiqinote.po.Note
import com.qiqinote.po.User
import com.qiqinote.service.NoteService
import com.qiqinote.service.UserService
import com.qiqinote.util.DateUtil
import com.qiqinote.util.EntityUtil
import com.qiqinote.util.TemplateUtil
import com.qiqinote.vo.NoteHomeVO
import com.qiqinote.vo.NoteViewVO
import com.qiqinote.vo.ResultVO
import com.qiqinote.vo.UserSimpleVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.io.BufferedOutputStream
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by vanki on 2018/3/12 14:20.
 */
@Controller
@RequestMapping("/note")
class NoteController @Autowired constructor(
        private val userService: UserService,
        private val noteService: NoteService
) : BaseController() {
    @GetMapping("/edit/{id}" + WebConst.needLoginHtmlSuffix)
    fun editHtml(@PathVariable("id") id: Long): ModelAndView {
        val loginUserId = this.getLoginUserId()
        val noteVO = this.noteService.getNoteVOById(loginUserId, id, null)
        if (noteVO == null || noteVO.needPwd == ServiceConst.falseVal) {
            throw QiqiNoteException(CodeEnum.NOT_FOUND)
        }
        val mv = ModelAndView(WebPageEnum.note_edit.url)
        mv.addObject("noteVO", noteVO)
        return mv
    }

    @GetMapping("/{id}" + WebConst.htmlSuffix)
    fun viewHtml(@PathVariable("id") id: Long, password: String?): ModelAndView {
        val loginUserId = this.justGetLoginUserId()
        val mv = ModelAndView(WebPageEnum.note_view.url)
        val noteVO = this.noteService.getNoteVOById(loginUserId, id, password)
                ?: throw QiqiNoteException(CodeEnum.NOT_FOUND)

        // 父节点数据
        noteVO.parentNote = this.noteService.getById(noteVO.note?.parentId ?: DBConst.defaultParentId)

        // 用户
        if (noteVO.note?.userId != null) {
            val user = this.userService.getById(noteVO.note?.userId!!)
            user?.let {
                noteVO.user = EntityUtil.copyValOfDiffObj(UserSimpleVO(), it)
            }
        }
        noteVO.createDatetimeStr = DateUtil.formatDatetime(noteVO.note?.createDatetime)
        noteVO.updateDatetimeStr = DateUtil.formatDatetime(noteVO.note?.updateDatetime)
        mv.addObject("noteVO", noteVO)
        return mv
    }

    @ResponseBody
    @PostMapping("/add" + WebConst.needLoginJsonSuffix)
    fun add(noteVO: NoteViewVO): ResultVO<Note?> {
        val result = this.noteService.add(this.getLoginUserId(), noteVO.note
                ?: Note(), noteVO.noteDetailList)
        if (!result.isSuccess()) return ResultVO(result.code, result.msg)
        return ResultVO(this.noteService.getById(result.data!!))
    }

    @ResponseBody
    @PostMapping("/updateById" + WebConst.needLoginJsonSuffix)
    fun updateById(noteVO: NoteViewVO) = this.noteService.updateById(this.getLoginUserId(), noteVO.note
            ?: Note(), noteVO.noteDetailList)

    @ResponseBody
    @GetMapping("/closeNote" + WebConst.jsonSuffix)
    fun closeNote(noteUserId: Long, id: Long): ResultVO<Any> {
        val loginUserId = this.justGetLoginUserId()
        if (noteUserId == loginUserId) {
            this.noteService.closeNoteInRedis(loginUserId, id)
        }
        return ResultVO()
    }

    @ResponseBody
    @GetMapping("/openNote" + WebConst.jsonSuffix)
    fun openNote(noteUserId: Long, id: Long): ResultVO<Any> {
        val loginUserId = this.justGetLoginUserId()
        if (noteUserId == loginUserId) {
            this.noteService.openNoteInRedis(loginUserId, id)
        }
        return ResultVO()
    }

    @ResponseBody
    @PostMapping("/deleteById" + WebConst.needLoginJsonSuffix)
    fun deleteById(id: Long) = this.noteService.deleteById(this.getLoginUserId(), id)


    @ResponseBody
    @GetMapping("/getNoteVOById" + WebConst.jsonSuffix)
    fun getNoteVoById(id: Long, password: String?): ResultVO<Any> {
        val vo = this.noteService.getNoteVOById(this.justGetLoginUserId(), id, password)
                ?: throw QiqiNoteException(CodeEnum.NOT_FOUND)
        if (vo.needPwd == ServiceConst.trueVal) {
            return ResultVO(CodeEnum.NOTE_PWD_ERROR)
        }
        // 父节点数据
        vo.parentNote = this.noteService.getById(vo.note?.parentId ?: DBConst.defaultParentId)

        vo.createDatetimeStr = DateUtil.formatDate(vo.note?.createDatetime)
        vo.updateDatetimeStr = DateUtil.formatDate(vo.note?.updateDatetime)
        // 用户
        if (vo.note?.userId != null) {
            val user = this.userService.getById(vo.note?.userId!!)
            if (user != null) {
                vo.user = EntityUtil.copyValOfDiffObj(UserSimpleVO(), user)
            }
        }
        return ResultVO(vo)
    }

    @ResponseBody
    @GetMapping("/pageOfHome.json")
    fun pageOfHome(currPage: Int?, pageSize: Int?, navNum: Int?): ResultVO<Page<NoteHomeVO>> {
        val page = this.noteService.page(this.justGetLoginUserId(), null, null,
                null, currPage ?: Page.firstPage, pageSize ?: 10, navNum ?: 3, "view_num DESC, id ASC")
        val noteList = page.data

        val returnPage = Page<NoteHomeVO>()
        EntityUtil.copyVal(returnPage, page, "data")
        val returnList = ArrayList<NoteHomeVO>()
        returnPage.data = returnList

        if (noteList.isEmpty()) return ResultVO(returnPage)

        val userTmpMap = hashMapOf<Long, UserSimpleVO>()
        val parentNoteMap = hashMapOf<Long, Note>()
        var noteHomeVO: NoteHomeVO
        var userIdTmp: Long?
        var userTmp: User?
        var userSimpleTmp: UserSimpleVO?
        var parentNoteIdTmp: Long?

        for (note in noteList) {
            userIdTmp = note.userId ?: continue

            noteHomeVO = NoteHomeVO()
            noteHomeVO.note = note

            /**
             * 添加用户信息
             */
            userSimpleTmp = userTmpMap[userIdTmp]
            if (userSimpleTmp == null) {
                userTmp = this.userService.getById(userIdTmp)
                if (userTmp == null) continue

                userSimpleTmp = EntityUtil.copyValOfDiffObj(UserSimpleVO(), userTmp)
                userTmpMap.put(userIdTmp, userSimpleTmp)
            }
            noteHomeVO.user = userSimpleTmp
            returnList.add(noteHomeVO)

            /**
             * 添加父笔记信息
             */
            parentNoteIdTmp = note.parentId ?: continue
            if (parentNoteIdTmp == DBConst.defaultParentId) continue
            var pNote: Note? = parentNoteMap[parentNoteIdTmp]
            if (pNote == null) {
                pNote = this.noteService.getById(parentNoteIdTmp)
                if (pNote == null) continue
                parentNoteMap.put(parentNoteIdTmp, pNote)
            }
            noteHomeVO.parentNote = pNote
        }
        return ResultVO(returnPage)
    }

    @ResponseBody
    @GetMapping("/listOfNoteTreeVO" + WebConst.jsonSuffix)
    fun listOfNoteTreeVO(userId: Long?, parentId: Long?): ResultVO<Any> {
        val loginUserId = this.justGetLoginUserId()
        if (userId == null && loginUserId == null) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }
        val userIdTmp = userId ?: loginUserId
        val list = this.noteService.listOfNoteTreeVO(loginUserId, userIdTmp!!, parentId)
        val totalNote = 0 //this.noteService.countNoteHasContent(userId)

        return ResultVO(mapOf("totalNote" to totalNote, "notes" to list))
    }

    @ResponseBody
    @RequestMapping("/download" + WebConst.jsonSuffix)
    fun downloadNote(id: Long, password: String?): ResultVO<Any> {
        val noteViewVo = this.noteService.getNoteVOById(this.justGetLoginUserId(), id, password)
        val detailList = noteViewVo?.noteDetailList
        if (noteViewVo?.needPwd != null && noteViewVo.needPwd == ServiceConst.trueVal) {
            return ResultVO(CodeEnum.NOTE_PWD_ERROR)
        }
        return if (detailList == null || detailList.isEmpty()) ResultVO(CodeEnum.NOTE_DOWNLOAD_FAIL) else ResultVO()

    }

    @ResponseBody
    @RequestMapping("/doDownload" + WebConst.jsonSuffix)
    fun doDownloadNote(id: Long, password: String?) {
        val noteTempList = TemplateUtil.getExportNoteTempList() ?: return

        val noteViewVo = this.noteService.getNoteVOById(this.justGetLoginUserId(), id, password)
        val detailList = noteViewVo?.noteDetailList
        if (noteViewVo?.needPwd != null && noteViewVo.needPwd == ServiceConst.trueVal) {
            return
        }
        if (detailList == null || detailList.isEmpty()) return

        val title = noteViewVo.note?.title ?: ""
        val htmlContent = StringBuffer()
        for (noteTemp in noteTempList) {
            when {
                TemplateUtil.key_exportNoteTemp_title.startsWith(noteTemp) -> htmlContent.append(title)
                TemplateUtil.key_exportNoteTemp_content.startsWith(noteTemp) -> htmlContent.append(detailList[0].content)
                else -> htmlContent.append(noteTemp)
            }
        }
        try {
            val byteArr = htmlContent.toString().toByteArray()
            response.reset()
            response.setHeader("Content-Disposition", "attachment; filename=\"${String(title.toByteArray(charset("UTF-8")), Charset.forName("ISO-8859-1"))}.html\"")
            response.addHeader("Content-Length", "" + byteArr.size)
            response.contentType = "application/octet-stream;charset=UTF-8"
            val outputStream = BufferedOutputStream(response.outputStream)

            outputStream.write(byteArr)

            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            log.error("", e)
        }
        return
    }
}

