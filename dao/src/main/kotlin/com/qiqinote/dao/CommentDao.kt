package com.qiqinote.dao

import com.qiqinote.po.Comment

/**
 * Created by vanki on 2018/5/2 18:06.
 */
interface CommentDao {
    fun create(comment: Comment): Long

    fun updateSubNum(id: Long, subNum: Int): Int

    fun delete(id: Long): Int

    fun getById(id: Long): Comment?

    fun list(targetId: Long, parentId: Long): MutableList<Comment>
}