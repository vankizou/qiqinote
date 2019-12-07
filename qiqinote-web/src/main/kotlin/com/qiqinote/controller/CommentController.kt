package com.qiqinote.controller

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.WebConst
import com.qiqinote.po.Comment
import com.qiqinote.service.CommentService
import com.qiqinote.vo.ResultVO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * Created by vanki on 2018/5/23 15:20.
 */
@Api("评论相关接口")
@Controller
@RequestMapping("/comment")
class CommentController @Autowired constructor(
        private val commentService: CommentService
) : BaseController() {

    @ResponseBody
    @PostMapping("/create" + WebConst.needLoginJsonSuffix)
    fun create(comment: Comment): ResultVO<Comment> {
        comment.fromUserId = this.getLoginUserId()
        return this.commentService.create(comment)
    }

    @ResponseBody
    @PostMapping("/delete" + WebConst.needLoginJsonSuffix)
    fun delete(type: Int, id: Long) = if (this.commentService.delete(this.getLoginUserId(), type, id) > 0) ResultVO<Any>() else ResultVO(CodeEnum.FAIL)

    @ResponseBody
    @GetMapping("/countRoot" + WebConst.jsonSuffix)
    fun countRoot(type: Int, targetId: Long) = ResultVO(this.commentService.countRoot(type, targetId))

    @ResponseBody
    @GetMapping("/listOfTarget" + WebConst.jsonSuffix)
    fun listOfTargetCommentDTO(type: Int, targetId: Long, rootId: Long, currPage: Int, pageSize: Int) = ResultVO(this.commentService.listOfTarget(type, targetId, rootId, currPage, pageSize, this.justGetLoginUserId()))

    @ResponseBody
    @GetMapping("/pageOfUnread" + WebConst.needLoginJsonSuffix)
    fun pageOfUnread(type: Int, pageSize: Int) = ResultVO(this.commentService.pageOfUnread(this.getLoginUserId(), type, pageSize))

    @ApiOperation("未读消息数量")
    @ResponseBody
    @GetMapping("/unreadNum" + WebConst.jsonSuffix)
    fun unreadNum(type: Int) = ResultVO(if (this.justGetLoginUserId() == null) 0 else this.commentService.unreadNum(this.getLoginUserId(), type))
}
