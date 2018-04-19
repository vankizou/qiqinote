package com.qiqinote.controller

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.ServiceConst
import com.qiqinote.constant.WebConst
import com.qiqinote.po.FindPwdQuestion
import com.qiqinote.po.User
import com.qiqinote.service.FindPwdQuestionService
import com.qiqinote.service.UserService
import com.qiqinote.util.PasswordUtil
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by vanki on 2018/3/13 17:35.
 */
@Controller
@RequestMapping("/user")
class UserController @Autowired constructor(
        private val userService: UserService,
        private val findPwdQuestionService: FindPwdQuestionService
) : BaseController() {

    @PostMapping("/update${WebConst.needLoginJsonSuffix}")
    fun update(user: User): ResultVO<Any> {
        user.id = this.getLoginUserId()
        return ResultVO(this.userService.upsertUser(user))
    }

    @PostMapping("/updatePwdQuestion${WebConst.needLoginJsonSuffix}")
    fun updatePwdQuestion(questions: List<FindPwdQuestion>): ResultVO<List<FindPwdQuestion>> {
        if (questions.size != ServiceConst.findPwdQuestionNum) return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_NUM_NOT_ENOUGH)
        return this.findPwdQuestionService.upsert(this.getLoginUserId(), questions)
    }

    @PostMapping("/listOfPwdQuestion${WebConst.needLoginJsonSuffix}")
    fun listOfPwdQuestion(account: String?, password: String?): ResultVO<List<FindPwdQuestion>> {
        var accountTmp = account
        if (accountTmp == null) accountTmp = this.justGetLoginUserId()?.toString()
        if (accountTmp == null) return ResultVO(CodeEnum.PARAM_ERROR)

        val user = this.userService.getByAccount(accountTmp) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)

        password?.let {
            if (PasswordUtil.getEncPwd(password) != user.password) return ResultVO(CodeEnum.PWD_ERROR)
        }
        val list = this.findPwdQuestionService.list(user.id!!)
        if (password == null) {
            list.forEach { it.answer = null }
        }
        return ResultVO(list)
    }

    @PostMapping("/updatePwdByOldPwd${WebConst.jsonSuffix}")
    fun updatePwdByOldPwd(account: String?, oldPassword: String, newPassword: String): ResultVO<Long> {
        var accountTmp = account
        if (accountTmp == null) accountTmp = this.justGetLoginUserId()?.toString()
        if (StringUtil.isEmpty(accountTmp) || StringUtil.isEmpty(newPassword)) return ResultVO(CodeEnum.PARAM_ERROR)

        val user = this.userService.getByAccount(accountTmp!!) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        if (PasswordUtil.getEncPwd(oldPassword) != user.password) return ResultVO(CodeEnum.PWD_ERROR)

        user.password = newPassword
        return this.userService.upsertUser(user)
    }

    @PostMapping("/updatepwdByQuestions${WebConst.jsonSuffix}")
    fun updatePwdByQuestions(account: String?, oldPassword: String, questions: List<FindPwdQuestion>): ResultVO<Long> {
        var accountTmp = account
        if (accountTmp == null) accountTmp = this.justGetLoginUserId()?.toString()
        if (StringUtil.isEmpty(accountTmp)) return ResultVO(CodeEnum.PARAM_ERROR)

        val questionMap = mutableMapOf<Long, String?>()
        questions.forEach {
            if (it.id != null) {
                questionMap[it.id!!] = it.answer?.trim()
            }
        }
        if (questionMap.size < ServiceConst.findPwdQuestionRightMinNum) return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_NUM_NOT_ENOUGH)

        val user = this.userService.getByAccount(accountTmp!!) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        val answers = this.findPwdQuestionService.list(user.id!!)

        var rightCount = 0
        answers.forEach {
            if (it.answer == questionMap[it.id]) rightCount++
        }
        if (rightCount < ServiceConst.findPwdQuestionRightMinNum) {
            return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_ANSWER_ERROR)
        }
        user.password = oldPassword
        return this.userService.upsertUser(user)
    }
}