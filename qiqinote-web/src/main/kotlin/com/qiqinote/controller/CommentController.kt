package com.qiqinote.controller

import com.qiqinote.anno.NeedLogin
import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.dto.TargetCommentDTO
import com.qiqinote.dto.UserUnreadCommentDTO
import com.qiqinote.po.Comment
import com.qiqinote.service.CommentService
import com.qiqinote.vo.ResultVO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by vanki on 2018/5/23 15:20.
 */
@Api("评论相关接口")
@RestController
@RequestMapping("/comment")
class CommentController @Autowired constructor(
        private val commentService: CommentService
) : BaseController() {

    @NeedLogin
    @ApiOperation("创建评论")
    @ApiImplicitParams(
            ApiImplicitParam(name = "type", value = "评论的对象类型。1：笔记"),
            ApiImplicitParam(name = "targetId", value = "评论的对象id，如：对象为笔记，则为笔记id")
    )
    @PostMapping("/create")
    fun create(comment: Comment): ResultVO<Comment> {
        comment.fromUserId = this.getLoginUserId()
        return this.commentService.create(comment)
    }

    @NeedLogin
    @ApiOperation("删除评论")
    @PostMapping("/delete")
    fun delete(type: Int, id: Long): ResultVO<Unit> {
        return if (this.commentService.delete(this.getLoginUserId(), type, id) > 0) {
            ResultVO()
        } else {
            ResultVO(CodeEnum.FAIL)
        }
    }

    @ApiOperation("某节点评论数量")
    @ApiImplicitParam(name = "rootId", value = "非必填，当值为[null]或[-1]时为根评论数")
    @GetMapping("/countRoot")
    fun subCount(type: Int, targetId: Long, rootId: Long?): ResultVO<Int> {
        return ResultVO(this.commentService.subCount(type, targetId, rootId))
    }

    @ApiOperation("评论列表")
    @GetMapping("/list")
    fun listOfTargetCommentDTO(type: Int, targetId: Long, rootId: Long?, page: Int, row: Int): ResultVO<List<TargetCommentDTO>> {
        return ResultVO(
                this.commentService.listOfTarget(
                        type,
                        targetId,
                        rootId ?: DBConst.defaultParentId,
                        page,
                        row,
                        this.justGetLoginUserId()
                )
        )
    }

    @NeedLogin
    @GetMapping("/unreads")
    fun unreads(type: Int, row: Int): ResultVO<List<UserUnreadCommentDTO>> {
        return ResultVO(this.commentService.unreads(this.getLoginUserId(), type, row))
    }

    @ApiOperation("未读消息数量")
    @GetMapping("/unreadNum")
    fun unreadNum(type: Int): ResultVO<Long> {
        return ResultVO(
                if (this.justGetLoginUserId() == null) {
                    0
                } else {
                    this.commentService.unreadNum(this.getLoginUserId(), type)
                }
        )
    }
}
