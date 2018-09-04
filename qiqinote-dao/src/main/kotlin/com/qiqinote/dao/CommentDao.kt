package com.qiqinote.dao

import com.qiqinote.dto.TargetCommentDTO
import com.qiqinote.dto.UserUnreadCommentDTO
import com.qiqinote.po.Comment

/**
 * Created by vanki on 2018/5/2 18:06.
 */
interface CommentDao {
    fun create(comment: Comment): Long

    fun updateSubNumByRootId(id: Long): Int

    fun delete(id: Long): Int

    fun getById(id: Long): Comment?

    fun countRoot(type: Int, targetId: Long): Int

    fun listOfUserUnreadCommentDTO(type: Int, ids: MutableList<Long>): MutableList<UserUnreadCommentDTO>

    fun listOfUserUnreadCommentDTO(type: Int, ids: MutableSet<String>): MutableList<UserUnreadCommentDTO>

    fun listOfTargetCommentDTO(type: Int, targetId: Long, rootId: Long, orderBy: String?, currPage: Int, pageSize: Int): MutableList<TargetCommentDTO>

    fun listOfTargetCommentDTO(ids: List<Long>): MutableList<TargetCommentDTO>
}