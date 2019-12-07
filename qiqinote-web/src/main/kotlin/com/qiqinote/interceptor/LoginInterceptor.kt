package com.qiqinote.interceptor

import com.qiqinote.anno.NeedLogin
import com.qiqinote.service.UserService
import com.qiqinote.util.WebUtil
import org.apache.log4j.Logger
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
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

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setCharacterEncoding("UTF-8")
        response.setCharacterEncoding("UTF-8")
        response.setContentType("application/json;charset=UTF-8")

        val needLogin =
                if (handler is HandlerMethod) {
                    handler.getMethodAnnotation(NeedLogin::class.java) != null
                } else {
                    false
                }

        if (!needLogin) {
            return true
        }

        val isLogin = autoSignIn(request, response)
        if (isLogin) {
            return true
        }

        /**
         * 需要登录
         */
        // request.servletPath ?: "/"
        WebUtil.buildLoginException()
        return true
    }

    /**
     * 自动登录
     */
    private fun autoSignIn(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        val uc = this.userService.getUserContextVO(request, response, true)
        return uc != null
    }
}