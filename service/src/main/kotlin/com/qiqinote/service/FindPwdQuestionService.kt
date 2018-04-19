package com.qiqinote.service

import com.qiqinote.po.FindPwdQuestion
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/4/19 10:51.
 */
interface FindPwdQuestionService {
    fun upsert(loginUserId: Long, questions: List<FindPwdQuestion>): ResultVO<List<FindPwdQuestion>>

    fun deleteOne(id: Long, loginUserId: Long): Int

    fun list(userId: Long): MutableList<FindPwdQuestion>
}