package com.qiqinote.controller

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.WebPageEnum
import com.qiqinote.vo.ResultVO
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import springfox.documentation.annotations.ApiIgnore

/**
 * Created by vanki on 2018/1/25 11:20.
 */
@ApiIgnore
@Controller
@RequestMapping("/")
class ExceptionController : ErrorController {

    override fun getErrorPath() = ""

    @RequestMapping("/error")
    fun error() = ResultVO<String>(CodeEnum.NOT_LOGIN.code, CodeEnum.NOT_LOGIN.msg)

    @RequestMapping("/404.html")
    fun _404() = ResultVO<String>(CodeEnum.NOT_LOGIN.code, CodeEnum.NOT_LOGIN.msg)

    @RequestMapping("/500.html")
    fun _500() = ResultVO<String>(CodeEnum.SYSTEM_ERROR.code, CodeEnum.SYSTEM_ERROR.msg)
}