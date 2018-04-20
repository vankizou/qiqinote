package com.qiqinote.service

import com.qiqinote.po.SecurityQuestion
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/4/19 10:51.
 */
interface SecurityQuestionService {
    fun upsert(loginUserId: Long, questions: List<SecurityQuestion>): ResultVO<List<SecurityQuestion>>

    fun deleteOne(id: Long, loginUserId: Long): Int

    fun list(userId: Long): MutableList<SecurityQuestion>
}