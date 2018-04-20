package com.qiqinote.controller

import com.qiqinote.constant.*
import com.qiqinote.dto.UserContext
import com.qiqinote.po.SecurityQuestion
import com.qiqinote.po.User
import com.qiqinote.service.SecurityQuestionService
import com.qiqinote.service.UserService
import com.qiqinote.util.PasswordUtil
import com.qiqinote.util.StringUtil
import com.qiqinote.util.WebUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

/**
 * Created by vanki on 2018/3/13 17:35.
 */
@Controller
@RequestMapping("/user")
class UserController @Autowired constructor(
        private val userService: UserService,
        private val securityQuestionService: SecurityQuestionService
) : BaseController() {

    @RequestMapping("/setting" + WebConst.htmlSuffix)
    fun settingHtml(): ModelAndView {
        val mv = ModelAndView(WebPageEnum.user_setting.url)
        return mv
    }

    @ResponseBody
    @GetMapping("/info" + WebConst.needLoginJsonSuffix)
    fun info() = ResultVO(this.userContext?.user)

    @ResponseBody
    @PostMapping("/updateInfo" + WebConst.needLoginJsonSuffix)
    fun update(user: User): ResultVO<Long> {
        user.id = this.getLoginUserId()
        user.password = null
        val result = this.userService.upsertUser(user)
        if (result.isSuccess()) {
            val newUser = this.userService.getById(this.getLoginUserId())
            this.request.session.setAttribute(WebKeyEnum.sessionUserContext.shortName, UserContext(newUser!!, this.userContext?.avatar))
        }
        return result
    }

    @ResponseBody
    @PostMapping("/updateSecurityQuestions" + WebConst.needLoginJsonSuffix)
    fun updateSecurityQuestions(password: String, questionDTO: SecurityQuestionDTO): ResultVO<List<SecurityQuestion>> {
        if (questionDTO.questions == null || questionDTO.questions?.size != ServiceConst.findPwdQuestionNum) return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_NUM_NOT_ENOUGH)

        val user = this.userService.getById(this.getLoginUserId()) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        if (PasswordUtil.getEncPwd(password) != user.password) return ResultVO(CodeEnum.PWD_ERROR)

        return this.securityQuestionService.upsert(this.getLoginUserId(), questionDTO.questions!!)
    }

    @ResponseBody
    @PostMapping("/updatePwdByOldPwd" + WebConst.jsonSuffix)
    fun updatePwdByOldPwd(account: String?, oldPassword: String, newPassword: String): ResultVO<Long> {
        var accountTmp = account
        if (accountTmp == null) accountTmp = this.justGetLoginUserId()?.toString()
        if (accountTmp == null || StringUtil.isEmpty(newPassword)) return ResultVO(CodeEnum.PARAM_ERROR)

        val user = this.userService.getByAccount(accountTmp, this.userContext?.user)
                ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        if (PasswordUtil.getEncPwd(oldPassword) != user.password) return ResultVO(CodeEnum.PWD_ERROR)

        user.password = newPassword
        return this.userService.upsertUser(user)
    }

    @ResponseBody
    @PostMapping("/updatePwdByQuestions" + WebConst.jsonSuffix)
    fun updatePwdByQuestions(account: String?, password: String, questionDTO: SecurityQuestionDTO): ResultVO<Long> {
        var accountTmp = account
        if (accountTmp == null) accountTmp = this.justGetLoginUserId()?.toString()
        if (accountTmp == null) return ResultVO(CodeEnum.PARAM_ERROR)

        val questionMap = mutableMapOf<Long, String?>()
        questionDTO.questions?.forEach {
            if (it.id != null) {
                questionMap[it.id!!] = it.answer?.trim()
            }
        }
        if (questionMap.size < ServiceConst.findPwdQuestionRightMinNum) return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_NUM_NOT_ENOUGH)

        val user = this.userService.getByAccount(accountTmp, this.userContext?.user)
                ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        val answers = this.securityQuestionService.list(user.id!!)

        var rightCount = 0
        answers.forEach {
            if (it.answer == questionMap[it.id]) rightCount++
        }
        if (rightCount < ServiceConst.findPwdQuestionRightMinNum) {
            return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_ANSWER_ERROR)
        }
        user.password = password
        return this.userService.upsertUser(user)
    }

    @ResponseBody
    @PostMapping("/listOfPwdQuestion" + WebConst.jsonSuffix)
    fun listOfPwdQuestion(account: String?, password: String?): ResultVO<List<SecurityQuestion>> {
        var accountTmp = account
        if (accountTmp == null) accountTmp = this.justGetLoginUserId()?.toString()
        if (accountTmp == null) return ResultVO(CodeEnum.PARAM_ERROR)

        val user = this.userService.getByAccount(accountTmp, this.userContext?.user)
                ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)

        val list = this.securityQuestionService.list(user.id!!)
        if (password == null || PasswordUtil.getEncPwd(password) != user.password) {
            list.forEach { it.answer = null }
        }
        return ResultVO(list)
    }

    class SecurityQuestionDTO() {
        var questions: MutableList<SecurityQuestion>? = null
    }
}