package com.qiqinote.controller

import com.qiqinote.anno.NeedLogin
import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.constant.ServiceConst
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.po.SecurityQuestion
import com.qiqinote.po.User
import com.qiqinote.service.SecurityQuestionService
import com.qiqinote.util.PasswordUtil
import com.qiqinote.util.RequestUtil
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.ResultVO
import com.qiqinote.vo.UserContextVO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.set

/**
 * Created by vanki on 2018/3/13 17:35.
 */
@Api("用户相关")
@RestController
@RequestMapping("/user")
class UserController @Autowired constructor(
        private val securityQuestionService: SecurityQuestionService
) : BaseController() {

    /**
     * 注册
     */
    @ApiOperation("注册")
    @ResponseBody
    @PostMapping("/signUp")
    fun signUp(name: String, alias: String?, password: String, imageCode: String): ResultVO<Long> {
        if (!this.validateImageCode(imageCode)) {
            return ResultVO(CodeEnum.IMAGE_CODE_ERROR)
        }

        val user = User()
        user.name = name
        user.alias = alias
        user.password = password
        user.registerIp = RequestUtil.getRequestIP(request)

        return userService.upsertUser(user)
    }

    /**
     * 登录
     */
    @ApiOperation("登录")
    @ApiImplicitParam(name = "isRemember", value = "是否记住登录，0/1，默认0")
    @ResponseBody
    @PostMapping("/signIn")
    fun signIn(account: String, password: String, isRemember: Int?): ResultVO<UserContextVO?> {
        var originTmp = DBConst.UserLoginRecord.originNone
        /**
         * 普通登录不存在自动登录
         */
        if (originTmp == DBConst.UserLoginRecord.originAutoLogin) {
            originTmp = DBConst.UserLoginRecord.originNone
        }
        val result = this.userService.signIn(
                this.request,
                this.response,
                account,
                password,
                isRemember == 1,
                originTmp
        )
        return if (result == null) {
            ResultVO(1001, "帐号或密码错误")
        } else {
            ResultVO(result)
        }
    }

    /**
     * 退出
     */
    @ApiOperation("退出登录")
    @ResponseBody
    @RequestMapping("/signOut", method = arrayOf(RequestMethod.GET, RequestMethod.POST))
    fun signOut(): ResultVO<Any> {
        this.userService.singOut(this.request, this.response)
        return ResultVO()
    }

    @NeedLogin
    @ApiOperation("当前登录用户信息")
    @ResponseBody
    @GetMapping("/currentInfo")
    fun info(): ResultVO<UserContextVO> {
        return ResultVO(this.userContext ?: throw QiqiNoteException(CodeEnum.NOT_LOGIN))
    }

    @NeedLogin
    @ApiOperation("修改用户信息")
    @ResponseBody
    @PostMapping("/updateInfo")
    fun update(
            avatarId: Long?,
            alias: String?,
            gender: Int?,
            motto: String?,
            phone: String?,
            email: String?,
            qq: String?,
            weixin: String?,
            weibo: String?,
            birthday: Date?,
            description: String?
    ): ResultVO<UserContextVO?> {
        val loginUserId = this.getLoginUserId()

        val user = User()
        user.id = loginUserId
        user.avatarId = avatarId
        user.alias = alias
        user.gender = gender
        user.motto = motto
        user.phone = phone
        user.email = email
        user.qq = qq
        user.weixin = weixin
        user.weibo = weibo
        user.birthday = birthday
        user.description = description

        val result = this.userService.upsertUser(user)
        if (!result.isSuccess()) {
            return ResultVO(result.code, result.msg)
        }
        val u = this.userService.getById(loginUserId)!!
        return ResultVO(
                this.userService.getUserContextVO(
                        this.request,
                        this.response,
                        loginUserId.toString(),
                        PasswordUtil.getDecPwd(u.password!!),
                        false,
                        null
                )
        )
    }

    @NeedLogin
    @ApiOperation("修改密保问题")
    @ResponseBody
    @PostMapping("/updateSecurityQuestions")
    fun updateSecurityQuestions(password: String, questionDTO: SecurityQuestionDTO): ResultVO<List<SecurityQuestion>> {
        if (questionDTO.questions == null || questionDTO.questions?.size != ServiceConst.findPwdQuestionNum) {
            return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_NUM_NOT_ENOUGH)
        }

        val user = this.userService.getById(this.getLoginUserId()) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        if (PasswordUtil.getEncPwd(password) != user.password) {
            return ResultVO(CodeEnum.PWD_ERROR)
        }

        return this.securityQuestionService.upsert(this.getLoginUserId(), questionDTO.questions!!)
    }

    @ApiOperation("修改登录密码（通过旧密码）")
    @ResponseBody
    @PostMapping("/updatePwdByOldPwd")
    fun updatePwdByOldPwd(account: String?, oldPassword: String, newPassword: String): ResultVO<Long> {
        var accountTmp = account
        if (accountTmp == null) {
            accountTmp = this.justGetLoginUserId()?.toString()
        }
        if (accountTmp == null || StringUtil.isEmpty(newPassword)) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }

        val user = this.userService.getByAccount(accountTmp) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        if (PasswordUtil.getEncPwd(oldPassword) != user.password) {
            return ResultVO(CodeEnum.PWD_ERROR)
        }

        user.password = newPassword
        val status = this.userService.upsertUser(user)
        if (status.isSuccess()) {
            this.userService.singOut(this.request, this.response)
        }
        return status
    }

    @ApiOperation("修改密码（通过密保问题）")
    @ResponseBody
    @PostMapping("/updatePwdByQuestions")
    fun updatePwdByQuestions(account: String?, password: String, questionDTO: SecurityQuestionDTO): ResultVO<Long> {
        var accountTmp = account
        if (accountTmp == null) {
            accountTmp = this.justGetLoginUserId()?.toString()
        }
        if (accountTmp == null) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }

        val questionMap = mutableMapOf<Long, String?>()
        questionDTO.questions?.forEach {
            if (it.id != null) {
                questionMap[it.id!!] = it.answer?.trim()
            }
        }
        if (questionMap.size < ServiceConst.findPwdQuestionRightMinNum) {
            return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_NUM_NOT_ENOUGH)
        }

        val user = this.userService.getByAccount(accountTmp) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)
        val answers = this.securityQuestionService.list(user.id!!)

        var rightCount = 0
        answers.forEach {
            if (it.answer == questionMap[it.id]) {
                rightCount++
            }
        }
        if (rightCount < ServiceConst.findPwdQuestionRightMinNum) {
            return ResultVO(CodeEnum.FIND_PWD_QUESTIONS_ANSWER_ERROR)
        }
        user.password = password
        val status = this.userService.upsertUser(user)
        if (status.isSuccess()) {
            this.userService.singOut(this.request, this.response)
        }
        return status
    }

    @ApiOperation("获取密保问题列表")
    @ApiImplicitParam(name = "password", value = "若密码为空或不正确，则不显示答案")
    @ResponseBody
    @PostMapping("/listOfPwdQuestion")
    fun listOfPwdQuestion(account: String?, password: String?): ResultVO<List<SecurityQuestion>> {
        var accountTmp = account
        if (accountTmp == null) {
            accountTmp = this.justGetLoginUserId()?.toString()
        }
        if (accountTmp == null) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }

        val user = this.userService.getByAccount(accountTmp) ?: return ResultVO(CodeEnum.USER_NOT_EXISTS)

        val list = this.securityQuestionService.list(user.id!!)
        if (password == null || PasswordUtil.getEncPwd(password) != user.password) {
            list.forEach { it.answer = null }
        }
        return ResultVO(list)
    }

    class SecurityQuestionDTO {
        var questions: MutableList<SecurityQuestion>? = null
    }
}