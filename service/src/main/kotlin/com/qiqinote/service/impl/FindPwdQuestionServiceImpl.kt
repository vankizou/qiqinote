package com.qiqinote.service.impl

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.ServiceConst
import com.qiqinote.dao.FindPwdQuestionDao
import com.qiqinote.po.FindPwdQuestion
import com.qiqinote.service.FindPwdQuestionService
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/4/19 10:52.
 */
@Service
class FindPwdQuestionServiceImpl @Autowired constructor(
        private val findPwdQuestionDao: FindPwdQuestionDao
) : FindPwdQuestionService {
    override fun upsert(loginUserId: Long, questions: List<FindPwdQuestion>): ResultVO<List<FindPwdQuestion>> {
        val questionsTmp = questions.filter { StringUtil.isNotBlank(it.question) && StringUtil.isNotBlank(it.answer) }
        if (questionsTmp.size != ServiceConst.findPwdQuestionNum) return ResultVO(CodeEnum.PARAM_ERROR)

        questionsTmp.forEach {
            it.userId = loginUserId
            this.findPwdQuestionDao.upsert(it)
        }
        return ResultVO(this.findPwdQuestionDao.list(loginUserId))
    }

    override fun deleteOne(id: Long, loginUserId: Long) = this.findPwdQuestionDao.deleteOne(id, loginUserId)

    override fun list(userId: Long) = this.findPwdQuestionDao.list(userId)
}