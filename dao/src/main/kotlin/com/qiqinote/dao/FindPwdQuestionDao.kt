package com.qiqinote.dao

import com.qiqinote.po.FindPwdQuestion

/**
 * Created by vanki on 2018/4/18 14:22.
 */
interface FindPwdQuestionDao {
    fun upsert(question: FindPwdQuestion): Int

    fun deleteOne(id: Long, userId: Long): Int

    fun getOne(id: Long, userId: Long): FindPwdQuestion?

    fun list(userId: Long): MutableList<FindPwdQuestion>
}