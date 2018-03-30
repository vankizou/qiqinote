package com.qiqinote.controller

import com.qiqinote.constant.WebKeyEnum
import com.qiqinote.dto.UserContext
import com.qiqinote.util.CookieUtil
import com.qiqinote.util.MD5Util
import com.qiqinote.util.UserUtil
import com.qiqinote.util.WebUtil
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/25 16:03.
 */
open class BaseController {
    protected val log = Logger.getLogger(this.javaClass)

    @Autowired
    protected lateinit var request: HttpServletRequest
    @Autowired
    protected lateinit var response: HttpServletResponse

    protected var userContext: UserContext? = null
        get() {
            field = UserUtil.getUCBySession(request)
            if ((field == null || field?.user == null)) {
                WebUtil.buildExceptionIfNeedLogin(request.servletPath)
            }
            return field
        }

    /**
     * 用户是否有操作权限
     */
    protected fun isPermission(userId: Long?) = if (userId == null) false else userId.equals(userContext?.user?.id)

    protected fun getLoginUserId() = justGetLoginUserId()!!

    protected fun justGetLoginUserId() = userContext?.user?.id

    protected fun validateImageCode(imageCode: String?): Boolean {
        if (imageCode == null) return false
        val cookieVal = CookieUtil.getCookie(request, WebKeyEnum.cookieImageCodeV.shortName) ?: return false
        return cookieVal.equals(MD5Util.getMD5(imageCode))
    }
}