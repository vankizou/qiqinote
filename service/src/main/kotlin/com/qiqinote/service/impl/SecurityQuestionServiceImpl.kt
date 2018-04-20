package com.qiqinote.service.impl

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.ServiceConst
import com.qiqinote.dao.SecurityQuestionDao
import com.qiqinote.po.SecurityQuestion
import com.qiqinote.service.SecurityQuestionService
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/4/19 10:52.
 */
@Service
class SecurityQuestionServiceImpl @Autowired constructor(
        private val securityQuestionDao: SecurityQuestionDao
) : SecurityQuestionService {
    override fun upsert(loginUserId: Long, questions: List<SecurityQuestion>): ResultVO<List<SecurityQuestion>> {
        val questionsTmp = questions.filter { StringUtil.isNotBlank(it.question) && StringUtil.isNotBlank(it.answer) }
        if (questionsTmp.size != ServiceConst.findPwdQuestionNum) return ResultVO(CodeEnum.PARAM_ERROR)

        questionsTmp.forEach {
            it.userId = loginUserId
            this.securityQuestionDao.upsert(it)
        }
        return ResultVO(this.securityQuestionDao.list(loginUserId))
    }

    override fun deleteOne(id: Long, loginUserId: Long) = this.securityQuestionDao.deleteOne(id, loginUserId)

    override fun list(userId: Long) = this.securityQuestionDao.list(userId)
}