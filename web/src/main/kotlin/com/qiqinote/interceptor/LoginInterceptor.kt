package com.qiqinote.interceptor

import com.qiqinote.constant.DBConst
import com.qiqinote.service.UserService
import com.qiqinote.util.UserUtil
import com.qiqinote.util.WebUtil
import org.apache.log4j.Logger
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/24 14:19.
 */
@Component
class LoginInterceptor(
        private val userService: UserService
) : HandlerInterceptor {
    private val log = Logger.getLogger(LoginInterceptor::class.java)
    private val excludeSuffixs = mutableListOf<String>()

    init {
        excludeSuffixs.add("/")
        excludeSuffixs.add(".json")
        excludeSuffixs.add(".html")

        /* 静态文件 */
        excludeSuffixs.add(".js")
        excludeSuffixs.add(".css")
        excludeSuffixs.add(".jpg")
        excludeSuffixs.add(".jpeg")
        excludeSuffixs.add(".png")
        excludeSuffixs.add(".ico")
        excludeSuffixs.add(".gif")
        excludeSuffixs.add(".mp4")
        excludeSuffixs.add(".md")

        /* 字体 */
        excludeSuffixs.add(".ttf")
        excludeSuffixs.add(".eot")
        excludeSuffixs.add(".woff")
        excludeSuffixs.add(".woff2")
        excludeSuffixs.add(".svg")

        /* 异常 */
        excludeSuffixs.add("/error")
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setCharacterEncoding("UTF-8")
        response.setCharacterEncoding("UTF-8")
        response.setContentType("application/json;charset=UTF-8")

        var requestURI = request.servletPath ?: "/"

        val isLogin = autoSignIn(request, response)
        if (isLogin) return true

        /**
         * 需要登录
         */
        WebUtil.buildExceptionIfNeedLogin(requestURI)
        return true
    }

    /**
     * 自动登录
     */
    private fun autoSignIn(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        var uc = UserUtil.getUCBySession(request)
        if (uc != null) return true

        val userIdAndPwd = UserUtil.getUserIdAndPwdByCookie(request)

        userIdAndPwd?.let {
            val isSuccess = UserUtil.signIn(request, response, userService, userIdAndPwd[0],
                    userIdAndPwd[1], DBConst.trueVal, DBConst.UserLoginRecord.originAutoLogin)
            if (isSuccess) return true
        }

        WebUtil.doSignOut(response)
        return false
    }
}