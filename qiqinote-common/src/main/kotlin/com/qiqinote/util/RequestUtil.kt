package com.qiqinote.util

import javax.servlet.http.HttpServletRequest

/**
 * Created by vanki on 2018/1/25 16:14.
 */
object RequestUtil {
    /**
     * 获取远程访问IP地址
     * @return
     */
    fun getRequestIP(request: HttpServletRequest): String? {
        var ip: String? = request.getHeader("x-forwarded-for")
        if (ip == null || ip.length == 0 || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip == null || ip.length == 0 || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip == null || ip.length == 0 || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        if (ip != null) {
            val ipArray = ip.split(',', limit = 2)
            if (ipArray.isNotEmpty()) ip = ipArray[0]
        }
        return ip
    }
}