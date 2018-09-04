package com.qiqinote.service

import com.qiqinote.dto.TargetCommentDTO
import com.qiqinote.dto.UserUnreadCommentDTO
import com.qiqinote.model.Page
import com.qiqinote.po.Comment
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/5/3 15:50.
 */
interface CommentService {
    fun create(comment: Comment): ResultVO<Comment>

    fun delete(loginUserId: Long, type: Int, id: Long): Int

    fun getById(id: Long): Comment?

    fun countRoot(type: Int, targetId: Long): Int

    fun listOfTarget(type: Int, rootId: Long, ids: List<Long>, loginUserId: Long?): MutableList<TargetCommentDTO>

    fun listOfTarget(type: Int, targetId: Long, rootId: Long, currPage: Int, pageSize: Int, loginUserId: Long?): MutableList<TargetCommentDTO>

    fun unreadNum(userId: Long, type: Int): Long

    fun pageOfUnread(userId: Long, type: Int, pageSize: Int): Page<UserUnreadCommentDTO>
}