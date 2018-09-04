package com.qiqinote.constant

/**
 * Created by vanki on 2018/1/24 17:41.
 */
object WebConst {
    /**
     * html后缀, 跳转页面
     */
    const val htmlSuffix = ".html"

    /**
     * json后缀, 返回未登录code
     */
    const val jsonSuffix = ".json"

    /**
     * 需要登录html后缀, 跳转页面
     */
    const val needLoginHtmlSuffix = ".shtml"

    /**
     * 需要登录json后缀, 返回未登录code
     */
    const val needLoginJsonSuffix = ".sjson"

    /**
     * 图片验证码cookie失效时间
     */
    const val cookieImageCodeVInvalidTime = 60 * 10
}