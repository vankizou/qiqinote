package com.qiqinote.util

import java.net.URLDecoder
import java.net.URLEncoder
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/24 14:57.
 */
object CookieUtil {
    fun setCookie(response: HttpServletResponse?, name: String, value: String?) = setCookie(response, name, value, 60 * 60 * 24 * 3650)

    fun setCookie(response: HttpServletResponse?, name: String, value: String?, maxAge: Int): Boolean {
        if (value == null) return false
        val cookie = Cookie(name, URLEncoder.encode(value, "UTF-8"))
        cookie.maxAge = maxAge
        cookie.path = "/"
        response?.addCookie(cookie)
        return true
    }

    fun getCookie(request: HttpServletRequest?, name: String): String? {
        val cookieList = listCookie(request)

        cookieList?.iterator()?.forEach {
            if (name == it.name) return URLDecoder.decode(it.value ?: "", "UTF-8")
        }
        return null
    }

    fun listCookie(request: HttpServletRequest?) = request?.cookies

    fun deleteCookie(response: HttpServletResponse?, name: String) = setCookie(response, name, "", 0)
}