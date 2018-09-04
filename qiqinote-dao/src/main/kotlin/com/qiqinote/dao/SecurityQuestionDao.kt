package com.qiqinote.dao

import com.qiqinote.po.SecurityQuestion

/**
 * Created by vanki on 2018/4/18 14:22.
 */
interface SecurityQuestionDao {
    fun upsert(question: SecurityQuestion): Int

    fun deleteOne(id: Long, userId: Long): Int

    fun getOne(id: Long, userId: Long): SecurityQuestion?

    fun list(userId: Long): MutableList<SecurityQuestion>
}