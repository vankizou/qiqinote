package com.qiqinote.exception

import com.qiqinote.constant.CodeEnum
import com.qiqinote.util.WebUtil
import com.qiqinote.vo.ResultVO
import org.apache.log4j.Logger
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/22 18:48.
 */
@ControllerAdvice
class ExceptionHandler {
    private val log = Logger.getLogger(com.qiqinote.exception.ExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception, request: HttpServletRequest, response: HttpServletResponse): Any? {
        request.characterEncoding = "UTF-8"
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json;charset=UTF-8"

        when (ex) {
            is QiqiNoteException -> {
                WebUtil.printResponseData(response, ResultVO(ex.code, ex.msg))
            }
            is IllegalArgumentException -> {
                log.error("参数异常", ex)
                WebUtil.printResponseData(response, ResultVO(CodeEnum.PARAM_ERROR))
            }
            else -> {
                log.error("", ex)
                WebUtil.printResponseData(response, ResultVO(CodeEnum.FAIL))
            }
        }
        return null
    }
}