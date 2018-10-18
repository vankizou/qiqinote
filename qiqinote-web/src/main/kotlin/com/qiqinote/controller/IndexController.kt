package com.qiqinote.controller

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.constant.WebConst
import com.qiqinote.constant.WebPageEnum
import com.qiqinote.dto.UserDTO
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.po.User
import com.qiqinote.service.ConfigService
import com.qiqinote.service.NoteService
import com.qiqinote.service.UserService
import com.qiqinote.util.RequestUtil
import com.qiqinote.util.UserUtil
import com.qiqinote.util.WebUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.get
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by vanki on 2018/1/24 14:29.
 */
@Controller
@RequestMapping("/")
class IndexController @Autowired constructor(
        private val userService: UserService,
        private val noteService: NoteService,
        private val configService: ConfigService
) : BaseController() {

    @RequestMapping("/")
    fun index(): Any {
        return if (this.userContext != null) {
            "redirect:/${URLEncoder.encode(this.userContext?.user!!.name, "UTF-8")}"
        } else {
            val mv = ModelAndView(WebPageEnum.index.url)
            buildHomeData(mv)
            mv
        }
    }

    @RequestMapping("/index" + WebConst.htmlSuffix)
    fun index2(): ModelAndView {
        val mv = ModelAndView(WebPageEnum.index.url)
        buildHomeData(mv)
        return mv
    }

    private fun buildHomeData(mv: ModelAndView) {
//        mv.addObject("data", this.noteController.pageOfHome(1, 20, 3, null).data!!)
        mv.addObject("newest", this.noteService.page(null, null, null, null, "id DESC", false, null, 1, 15).data)
        mv.addObject("hottest", this.noteService.page(null, null, null, null, "view_num DESC", false, null, 1, 15).data)
    }

    @RequestMapping("/login" + WebConst.htmlSuffix)
    fun login() = ModelAndView(WebPageEnum.login.url)

    @RequestMapping("/{idOrName}")
    fun userHome(@PathVariable("idOrName") idOrName: String) = this.userHome(idOrName, "")

    @RequestMapping("/{idOrName}/{search}")
    fun userHome(@PathVariable("idOrName") idOrName: String, @PathVariable("search") search: String): ModelAndView {
        var user = this.userContext?.user
        if (user != null) {
            if (idOrName.toLongOrNull() != user.id && idOrName != user.name) {
                user = null
            }
        }
        if (user == null) {
            user = this.userService.getByAccount(idOrName)
        }
        if (user == null) {
            throw QiqiNoteException(CodeEnum.NOT_FOUND)
        }
        val mv = ModelAndView(WebPageEnum.note_list.url)
        mv.addObject("userId", user.id ?: 0)
        mv.addObject("userName", user.name ?: "")
        mv.addObject("userAlias", user.alias ?: "")
        mv.addObject("search", URLDecoder.decode(search, "UTF-8"))

        if (user.id != null && user.id == this.justGetLoginUserId()) {
            mv.addObject("isMine", true)

            val config = this.configService.getByUserId(user.id!!)
            if (config?.noteTreeConfig != null) {
                mv.addObject("treeCssCls", config.noteTreeConfig!!.treeCssCls)
                mv.addObject("contentCssCls", config.noteTreeConfig!!.contentCssCls)
            }
        }
        return mv
    }

    /**
     * 注册
     */
    @ResponseBody
    @RequestMapping("/signUp" + WebConst.jsonSuffix)
    fun signUp(name: String, alias: String?, password: String, imageCode: String): ResultVO<Any?> {
        if (!this.validateImageCode(imageCode)) return ResultVO(CodeEnum.IMAGE_CODE_ERROR)

        var user = User()
        user.name = name
        user.alias = alias
        user.password = password
        user.registerIp = RequestUtil.getRequestIP(request)

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
    fun signIn(account: String, password: String, isRemember: Int?, origin: Int?): ResultVO<Any> {
        if (this.userContext != null) return ResultVO(this.userContext!!)

        val isRememberTmp = isRemember ?: 0
        var originTmp = origin ?: DBConst.UserLoginRecord.originNone
        /**
         * 普通登录不存在自动登录
         */
        if (originTmp == DBConst.UserLoginRecord.originAutoLogin) {
            originTmp = DBConst.UserLoginRecord.originNone
        }
        return if (UserUtil.signIn(this.request, this.response, this.userService, account, password, isRememberTmp, originTmp, env["qiqinote.image.domain"])) ResultVO() else ResultVO(CodeEnum.FAIL)
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