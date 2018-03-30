package com.qiqinote.po

import com.qiqinote.util.RequestUtil
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by vanki on 2018/1/18 17:41.
 */
class UserLoginRecord {
    var id: Long? = null

    var userId: Long? = null

    var origin: Int? = null

    var ip: String? = null

    var protocol: String? = null

    var scheme: String? = null

    var serverName: String? = null

    var remoteAddr: String? = null

    var remoteHost: String? = null

    var characterEncoding: String? = null

    var accept: String? = null

    var acceptEncoding: String? = null

    var acceptLanguage: String? = null

    var userAgent: String? = null

    var connection: String? = null

    var createDatetime: Date? = null

    companion object {
        fun buildRequestInfo(request: HttpServletRequest): UserLoginRecord {
            val record = UserLoginRecord()
            record.createDatetime = Date()
            record.ip = RequestUtil.getRequestIP(request)
            record.protocol = request.protocol
            record.scheme = request.scheme
            record.serverName = request.serverName
            record.remoteAddr = request.remoteAddr
            record.remoteHost = request.remoteHost
            record.characterEncoding = request.characterEncoding
            record.accept = request.getHeader("Accept")
            record.acceptLanguage = request.getHeader("Accept-Language")
            record.acceptEncoding = request.getHeader("Accept-Encoding")
            record.userAgent = request.getHeader("User-Agent")
            record.connection = request.getHeader("Connection")
            return record
        }
    }
}