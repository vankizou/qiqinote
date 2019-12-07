package com.qiqinote.controller

import com.qiqinote.anno.NeedLogin
import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.constant.ServiceConst
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.po.Note
import com.qiqinote.po.NoteDetail
import com.qiqinote.po.User
import com.qiqinote.service.NoteDetailService
import com.qiqinote.service.NoteService
import com.qiqinote.util.StringUtil
import com.qiqinote.util.TemplateUtil
import com.qiqinote.vo.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.BufferedOutputStream
import java.io.IOException
import java.nio.charset.Charset
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.hashMapOf
import kotlin.collections.mutableListOf
import kotlin.collections.set

/**
 * Created by vanki on 2018/3/12 14:20.
 */
@Api("笔记相关")
@RestController
@RequestMapping("/note")
class NoteController @Autowired constructor(
        private val noteService: NoteService,
        private val noteDetailService: NoteDetailService
) : BaseController() {

    @NeedLogin
    @ApiOperation("添加新笔记")
    @PostMapping("/add")
    fun add(p: UpsertNoteParam): ResultVO<Note?> {
        val result = this.noteService.add(
                this.getLoginUserId(),
                p.note ?: return ResultVO(CodeEnum.PARAM_ERROR),
                p.noteDetails
        )
        if (!result.isSuccess()) return ResultVO(result.code, result.msg)
        return ResultVO(this.noteService.getByIdOrIdLink(result.data!!))
    }

    class UpsertNoteParam {
        var note: Note? = null
        var noteDetails: List<NoteDetail>? = null
    }

    @NeedLogin
    @ApiOperation("修改笔记")
    @PostMapping("/updateById")
    fun updateById(p: UpsertNoteParam): ResultVO<Int> {
        return this.noteService.updateById(
                this.getLoginUserId(),
                p.note ?: return ResultVO(CodeEnum.PARAM_ERROR),
                p.noteDetails
        )
    }

    @ApiOperation("关闭笔记（笔记树）")
    @GetMapping("/closeNote")
    fun closeNote(noteUserId: Long, id: Long): ResultVO<Any> {
        val loginUserId = this.justGetLoginUserId()
        if (noteUserId == loginUserId) {
            this.noteService.closeNoteInRedis(loginUserId, id)
        }
        return ResultVO()
    }

    @ApiOperation("打开笔记（笔记树）")
    @GetMapping("/openNote")
    fun openNote(noteUserId: Long, id: Long): ResultVO<Any> {
        val loginUserId = this.justGetLoginUserId()
        if (noteUserId == loginUserId) {
            this.noteService.openNoteInRedis(loginUserId, id)
        }
        return ResultVO()
    }

    @NeedLogin
    @ApiOperation("删除笔记")
    @PostMapping("/deleteById")
    fun deleteById(id: Long) = this.noteService.deleteById(this.getLoginUserId(), id)

    @ApiOperation("获取笔记详情")
    @ApiImplicitParams(
            ApiImplicitParam(name = "idOrIdLink", value = "id或id映射字符串，当查看别人笔记且secret=3（私密笔记）时则必须为idLink"),
            ApiImplicitParam(name = "password", value = "当返回数据[needPwd=true]时，传入该值作检验")
    )
    @GetMapping("/info/{idOrIdLink}")
    fun getNoteVOByIdOrIdLink(
            @PathVariable("idOrIdLink") idOrIdLink: String,
            password: String?
    ): ResultVO<NoteViewVO> {
        val id = idOrIdLink.toLongOrNull()
        val vo =
                this.noteService.getNoteVOByIdOrIdLink(
                        this.justGetLoginUserId(),
                        id,
                        if (id == null) idOrIdLink else null,
                        password,
                        this.request,
                        this.response
                ) ?: throw QiqiNoteException(CodeEnum.NOT_FOUND)
        if (vo.needPwd == ServiceConst.trueVal) {
            return ResultVO(CodeEnum.PWD_ERROR)
        }
        // 父节点数据
        vo.parentNote = this.noteService.getByIdOrIdLink(vo.note?.parentId ?: DBConst.defaultParentId)
        return ResultVO(vo)
    }

    @ApiOperation("获取主页笔记列表")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", value = "页码，初始页为1"),
            ApiImplicitParam(name = "row", value = "每页最大数据量"),
            ApiImplicitParam(name = "search", value = "搜索标题含相关字符的笔记，非必填")
    )
    @GetMapping("/home")
    fun listOfHome(page: Int, row: Int, search: String?): ResultVO<List<NoteHomeVO>> {
        if (page <= 0 || row <= 0) {
            return ResultVO()
        }
        val notePair =
                this.noteService.listByCondition(
                        this.justGetLoginUserId(),
                        null,
                        null,
                        search,
                        "update_datetime DESC",
                        false,
                        page,
                        row,
                        false
                )
        val notes = notePair.right
        val userTmpMap = hashMapOf<Long, UserContextVO>()
        val parentNoteMap = hashMapOf<Long, Note>()
        var noteHomeVO: NoteHomeVO
        var userIdTmp: Long?
        var userTmp: User?
        var userDTOTmp: UserContextVO?
        var parentNoteIdTmp: Long?
        var noteContents: MutableList<NoteDetail>

        val results = mutableListOf<NoteHomeVO>()

        for (note in notes) {
            userIdTmp = note.userId ?: continue

            noteHomeVO = NoteHomeVO()
            noteHomeVO.note = note

            /**
             * 获取笔记详情
             */
            noteContents = this.noteDetailService.listByNoteId(note.id!!)
            if (noteContents.isEmpty()) continue
            noteHomeVO.noteContent = noteContents[0].content

            /**
             * 添加用户信息
             */
            userDTOTmp = userTmpMap[userIdTmp]
            if (userDTOTmp == null) {
                userTmp = this.userService.getById(userIdTmp)
                if (userTmp == null) continue

                userDTOTmp = this.userService.buildUserContext(userTmp, true)
                userTmpMap[userIdTmp] = userDTOTmp
            }

            noteHomeVO.user = userDTOTmp
            results.add(noteHomeVO)

            /**
             * 添加父笔记信息
             */
            parentNoteIdTmp = note.parentId ?: continue
            if (parentNoteIdTmp == DBConst.defaultParentId) continue
            var pNote: Note? = parentNoteMap[parentNoteIdTmp]
            if (pNote == null) {
                pNote = this.noteService.getByIdOrIdLink(parentNoteIdTmp)
                if (pNote == null) continue
                parentNoteMap[parentNoteIdTmp] = pNote
            }
            noteHomeVO.parentNote = pNote
        }
        return ResultVO(results)
    }

    @ApiOperation("笔记树列表")
    @ApiImplicitParams(
            ApiImplicitParam(name = "userId", value = "用户id"),
            ApiImplicitParam(name = "parentId", value = "笔记树父id，若是根笔记，不填或值为[-1]"),
            ApiImplicitParam(name = "search", value = "搜索标题含相关字符的笔记，非必填")
    )
    @GetMapping("/tree")
    fun listOfNoteTreeVO(userId: Long?, parentId: Long?, search: String?): ResultVO<NoteTreeVOAndTotalNote> {
        val loginUserId = this.justGetLoginUserId()
        if (userId == null && loginUserId == null) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }
        val parentIdTmp = parentId ?: DBConst.defaultParentId
        val userIdTmp = userId ?: loginUserId
        val result =
                if (userIdTmp == loginUserId && parentIdTmp == DBConst.defaultParentId &&
                        StringUtil.isNotEmpty(search?.trim())) {
                    this.noteService.listOfNoteTreeVOByTitleLike(loginUserId!!, search!!.trim())
                } else {
                    NoteTreeVOAndTotalNote(
                            this.noteService.listOfNoteTreeVO(loginUserId, userIdTmp!!, parentIdTmp),
                            if (parentId == DBConst.defaultParentId) {
                                this.noteService.countNoteHasContent(loginUserId, userId ?: loginUserId)
                            } else {
                                0
                            }
                    )
                }

        return ResultVO(result)
    }

    @ApiOperation("获取最新笔记列表")
    @ApiImplicitParams(
            ApiImplicitParam(name = "userId", value = "用户id，非必填"),
            ApiImplicitParam(name = "row", value = "获取数据量，默认10")
    )
    @GetMapping("/newest")
    fun newest(userId: Long?, row: Int?): ResultVO<List<Note>> {
        val rowTmp = row ?: 10
        if (rowTmp <= 0) {
            return ResultVO()
        }
        return ResultVO(
                this.noteService.listByCondition(
                        null,
                        userId,
                        null,
                        null,
                        "id DESC",
                        false,
                        1,
                        rowTmp,
                        false
                ).right
        )
    }

    @ApiOperation("获取最热笔记列表")
    @ApiImplicitParams(
            ApiImplicitParam(name = "userId", value = "用户id，非必填"),
            ApiImplicitParam(name = "row", value = "获取数据量，默认10")
    )
    @GetMapping("/hottest")
    fun hottest(userId: Long?, row: Int?): ResultVO<List<Note>> {
        val rowTmp = row ?: 10
        if (rowTmp <= 0) {
            return ResultVO()
        }
        return ResultVO(
                this.noteService.listByCondition(
                        null,
                        userId,
                        null,
                        null,
                        "view_num DESC",
                        false,
                        1,
                        rowTmp,
                        false
                ).right
        )
    }

    //    @GetMapping("/totalNote" + WebConst.jsonSuffix)
    fun totalNote(userId: Long?): ResultVO<Any> {
        val loginUserId = this.justGetLoginUserId()
        if (userId == null && loginUserId == null) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }
        return ResultVO(this.noteService.countNoteHasContent(loginUserId, userId ?: loginUserId))
    }

    //    @RequestMapping("/preDownload" + WebConst.jsonSuffix)
    fun downloadNote(id: Long, idLink: String?, password: String?): ResultVO<Any> {
        val noteViewVo = this.noteService.getNoteVOByIdOrIdLink(this.justGetLoginUserId(), id, idLink, password)
        val detailList = noteViewVo?.noteDetails
        if (noteViewVo?.needPwd != null && noteViewVo.needPwd == ServiceConst.trueVal) {
            return ResultVO(CodeEnum.PWD_ERROR)
        }
        return if (detailList == null || detailList.isEmpty()) ResultVO(CodeEnum.NOTE_DOWNLOAD_FAIL) else ResultVO()

    }

    //    @RequestMapping("/download" + WebConst.jsonSuffix)
    fun doDownloadNote(id: Long, idLink: String?, password: String?) {
        val noteTempList = TemplateUtil.getExportNoteTempList() ?: return

        val noteViewVo = this.noteService.getNoteVOByIdOrIdLink(this.justGetLoginUserId(), id, idLink, password)
        val detailList = noteViewVo?.noteDetails
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

