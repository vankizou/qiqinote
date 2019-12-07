package com.qiqinote.service

import com.qiqinote.dto.TargetCommentDTO
import com.qiqinote.dto.UserUnreadCommentDTO
import com.qiqinote.po.Comment
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/5/3 15:50.
 */
interface CommentService {
    fun create(comment: Comment): ResultVO<Comment>

    fun delete(loginUserId: Long, type: Int, id: Long): Int

    fun getById(id: Long): Comment?

    fun subCount(type: Int, targetId: Long, rootId: Long?): Int

    fun listOfTarget(type: Int, rootId: Long, ids: List<Long>, loginUserId: Long?): List<TargetCommentDTO>

    fun listOfTarget(type: Int, targetId: Long, rootId: Long, page: Int, row: Int, loginUserId: Long?): List<TargetCommentDTO>

    fun unreadNum(userId: Long, type: Int): Long

    fun unreads(userId: Long, type: Int, row: Int): List<UserUnreadCommentDTO>
}