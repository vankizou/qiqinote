package com.qiqinote.util

import com.qiqinote.constant.WebKeyEnum
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/24 15:22.
 */
object UserUtil {
    private const val userRememberCookieDelim = ','

    fun setLoginTokenInCookie(response: HttpServletResponse, token: String) {
        if (StringUtil.isBlank(token)) {
            return
        }
        CookieUtil.setCookie(response, WebKeyEnum.cookieLoginToken.shortName, token)
    }

    fun getLoginTokenFromCookie(request: HttpServletRequest): String? {
        return CookieUtil.getCookie(request, WebKeyEnum.cookieLoginToken.shortName)
    }

    fun setUserAccountInCookie(response: HttpServletResponse, account: String) {
        if (StringUtil.isBlank(account)) {
            return
        }
        CookieUtil.setCookie(response, WebKeyEnum.cookieUserAccount.shortName, account)
    }

    fun getUserAccountInCookie(request: HttpServletRequest): String? {
        return CookieUtil.getCookie(request, WebKeyEnum.cookieUserAccount.shortName)
    }

    fun setUserPwdInCookie(response: HttpServletResponse, pwd: String) {
        val date = Date()
        var value: String = StringBuilder()
                .append(date.time)
                .append(userRememberCookieDelim)
                .append(pwd)
                .toString()
        value = PasswordUtil.getEncPwd(value)
        CookieUtil.setCookie(response, WebKeyEnum.cookieUserPassword.shortName, value)
    }

    fun getUserPwdFromCookie(request: HttpServletRequest): String? {
        var value: String? = CookieUtil.getCookie(request, WebKeyEnum.cookieUserPassword.shortName) ?: return null

        try {
            value = PasswordUtil.getDecPwd(value?.trim() ?: "")
        } catch (e: Exception) {
            return null
        }
        val infos = value.split(userRememberCookieDelim, limit = 2)
        if (infos.isEmpty() || infos.size != 2) return null
        return infos[1]
    }
}