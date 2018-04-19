package com.qiqinote.controller

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.constant.WebConst
import com.qiqinote.constant.WebPageEnum
import com.qiqinote.dto.UserContext
import com.qiqinote.dto.UserDTO
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.po.User
import com.qiqinote.service.UserService
import com.qiqinote.util.UserUtil
import com.qiqinote.util.WebUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

/**
 * Created by vanki on 2018/1/24 14:29.
 */
@Controller
@RequestMapping("/")
class IndexController @Autowired constructor(
        private val userService: UserService,
        private val noteController: NoteController
) : BaseController() {

    @RequestMapping("/")
    fun index(): Any {
        return if (this.userContext != null) {
            "redirect:/${this.userContext!!.user.name}"
        } else {
            val mv = ModelAndView(WebPageEnum.index.url)
            mv.addObject("data", this.noteController.pageOfHome(1, 20, 3, null).data!!)
            mv
        }
    }

    @RequestMapping("/index" + WebConst.htmlSuffix)
    fun index2(): ModelAndView {
        val mv = ModelAndView(WebPageEnum.index.url)
        mv.addObject("data", this.noteController.pageOfHome(1, 20, 3, null).data!!)
        return mv
    }

    @RequestMapping("/{idOrName}")
    fun userHome(@PathVariable("idOrName") idOrName: String): ModelAndView {
        var userDTO: UserDTO? = this.userContext?.user
        if (userDTO != null) {
            if (idOrName.toLongOrNull() != userDTO.id && idOrName != userDTO.name) {
                userDTO = null
            }
        }
        var user: User? = null
        if (userDTO == null) {
            val userId = idOrName.toLongOrNull()

            if (userId == null) {
                user = this.userService.getByName(idOrName)
            } else {
                user = this.userService.getById(userId)
            }
        }
        if (userDTO == null && user == null) {
            throw QiqiNoteException(CodeEnum.NOT_FOUND)
        }
        val mv = ModelAndView(WebPageEnum.note_list.url)
        mv.addObject("userId", user?.id ?: userDTO?.id ?: 0)
        mv.addObject("userName", user?.name ?: userDTO?.name ?: "")
        mv.addObject("userAlias", user?.alias ?: userDTO?.alias ?: "")
        return mv
    }

    /**
     * 注册
     */
    @ResponseBody
    @RequestMapping("/signUp" + WebConst.jsonSuffix)
    fun signUp(name: String, alias: String, password: String, imageCode: String): ResultVO<Any?> {
        if (!this.validateImageCode(imageCode)) return ResultVO(CodeEnum.IMAGE_CODE_ERROR)

        var user = User()
        user.name = name
        user.alias = alias
        user.password = password

        val result = this.userService.upsertUser(user)
        if (!result.isSuccess()) return ResultVO(result.code, result.msg)

        val u = this.userService.getById(result.data!!)
        return ResultVO(if (u == null) null else UserDTO(u))
    }

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping("/signIn" + WebConst.jsonSuffix)
    fun signIn(account: String, password: String, isRemember: Int?, origin: Int?): ResultVO<UserContext> {
        if (this.userContext != null) return ResultVO(this.userContext!!)

        val isRememberTmp = isRemember ?: 0
        var originTmp = origin ?: DBConst.UserLoginRecord.originNone
        /**
         * 普通登录不存在自动登录
         */
        if (originTmp == DBConst.UserLoginRecord.originAutoLogin) {
            originTmp = DBConst.UserLoginRecord.originNone
        }
        return UserUtil.signIn(this.request, this.response, this.userService, account, password, isRememberTmp, originTmp)
    }

    /**
     * 退出
     */
    @ResponseBody
    @RequestMapping("/signOut" + WebConst.jsonSuffix)
    fun signOutJson(): ResultVO<Any> {
        WebUtil.doSignOut(this.response)
        this.request.session.invalidate()
        return ResultVO()
    }

    /**
     * 退出
     */
    @RequestMapping("/signOut" + WebConst.htmlSuffix)
    fun signOutHtml(): ModelAndView {
        WebUtil.doSignOut(this.response)
        this.request.session.invalidate()
        return ModelAndView("redirect:/")
    }

    @RequestMapping("/info/markdown/case" + WebConst.htmlSuffix)
    fun markdownCase() = ModelAndView("info/markdown")
}