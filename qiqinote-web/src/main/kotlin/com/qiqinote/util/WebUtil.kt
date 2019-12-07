package com.qiqinote.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qiqinote.constant.CodeEnum
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.vo.ResultVO
import org.apache.log4j.Logger
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
     * 未登录且请求需要登录, 响应对应信息
     */
    @Throws(QiqiNoteException::class)
    fun buildLoginException() {
        throw QiqiNoteException(CodeEnum.NOT_LOGIN)
    }
}