package com.qiqinote.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.WebConst
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.vo.ResultVO
import org.apache.log4j.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/25 19:08.
 */
object WebUtil {
    private val log = Logger.getLogger(WebUtil::class.java)
    /**
     * response响应数据
     */
    fun printResponseData(response: HttpServletResponse, resultVO: ResultVO<Any>) {
        response.writer.println(jacksonObjectMapper().writeValueAsString(resultVO))
        response.writer.flush()
    }

    /**
     * 响应登录页
     */
    fun redirectLoginHtml(request: HttpServletRequest, response: HttpServletResponse) {
        val path = request.getContextPath()
        val basePath = StringBuffer()
                .append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(path)
                .append("/")
                .toString()
        response.sendRedirect(basePath + "/login.html");
    }

    /**
     * 未登录且请求需要登录, 响应对应信息
     */
    fun buildExceptionIfNeedLogin(requestURI: String) {
        if (requestURI.endsWith(WebConst.needLoginJsonSuffix)) {
            log.warn("未登录, 响应登录code, 请求路径: ${requestURI}");
            throw QiqiNoteException(CodeEnum.NOT_LOGIN)
        } else if (requestURI.endsWith(WebConst.needLoginHtmlSuffix)) {
            log.warn("未登录, 跳转到登录页面, 请求路径: ${requestURI}");
            throw QiqiNoteException(CodeEnum.NOT_LOGIN_HTML)
        }
    }
}