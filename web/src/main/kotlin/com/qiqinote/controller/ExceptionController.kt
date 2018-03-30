package com.qiqinote.controller

import com.qiqinote.constant.WebPageEnum
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by vanki on 2018/1/25 11:20.
 */
@Controller
@RequestMapping("/")
class ExceptionController : ErrorController {

    override fun getErrorPath() = ""

    @RequestMapping("/error")
    fun error() = "redirect:/404.html"

    @RequestMapping("/404.html")
    fun _404() = WebPageEnum._404.url

    @RequestMapping("/500.html")
    fun _500() = WebPageEnum._500.url
}