package com.qiqinote.util

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/24 14:57.
 */
object CookieUtil {
    fun setCookie(response: HttpServletResponse?, name: String, value: String?) = setCookie(response, name, value, -1)

    fun setCookie(response: HttpServletResponse?, name: String, value: String?, maxAge: Int): Boolean {
        if (value == null) return false
        val cookie = Cookie(name, value)
        cookie.maxAge = maxAge
        cookie.path = "/"
        response?.addCookie(cookie)
        return true
    }

    fun getCookie(request: HttpServletRequest?, name: String): String? {
        val cookieList = listCookie(request)

        cookieList?.iterator()?.forEach {
            if (name.equals(it.name)) return it.value
        }
        return null
    }

    fun listCookie(request: HttpServletRequest?) = request?.cookies

    fun deleteCookie(response: HttpServletResponse?, name: String) = setCookie(response, name, "", 0)
}