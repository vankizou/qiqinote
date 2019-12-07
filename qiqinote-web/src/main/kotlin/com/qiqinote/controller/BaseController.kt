package com.qiqinote.controller

import com.qiqinote.constant.WebKeyEnum
import com.qiqinote.service.AbstractBaseService
import com.qiqinote.service.UserService
import com.qiqinote.util.CookieUtil
import com.qiqinote.util.DateUtil
import com.qiqinote.util.MD5Util
import com.qiqinote.util.WebUtil
import com.qiqinote.vo.UserContextVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/25 16:03.
 */
abstract class BaseController : AbstractBaseService() {
    @Autowired
    protected lateinit var request: HttpServletRequest
    @Autowired
    protected lateinit var response: HttpServletResponse
    @Autowired
    protected lateinit var userService: UserService

    protected var userContext: UserContextVO? = null
        get() {
            field = this.userService.getUserContextVO(this.request, this.response, true)
            return field
        }

    /**
     * 用户是否有操作权限
     */
    protected fun isMine(userId: Long?) = if (userId == null) false else userId == userContext?.id

    protected fun getLoginUserId() : Long {
        val loginUserId = justGetLoginUserId()
        if(loginUserId == null) {
            WebUtil.buildLoginException()
        }
        return loginUserId!!
    }

    protected fun justGetLoginUserId() = userContext?.id

    protected fun validateImageCode(imageCode: String?): Boolean {
        if (imageCode == null) return false
        val cookieVal = CookieUtil.getCookie(request, WebKeyEnum.cookieImageCodeValue.shortName) ?: return false
        return cookieVal.equals(MD5Util.getMD5(imageCode))
    }

    @InitBinder
    private fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(Date::class.java, CustomDateEditor(DateUtil.sdfDatetime, true))
    }
}