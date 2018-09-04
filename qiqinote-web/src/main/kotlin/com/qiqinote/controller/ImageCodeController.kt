package com.qiqinote.controller

import com.qiqinote.constant.WebConst
import com.qiqinote.constant.WebKeyEnum
import com.qiqinote.util.CookieUtil
import com.qiqinote.util.MD5Util
import com.qiqinote.util.imagecode.ImageCodeMathCalcUtil
import com.qiqinote.vo.ResultVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by vanki on 2018/2/26 14:06.
 */
@RestController
@RequestMapping("/imagecode")
class ImageCodeController : BaseController() {

    @GetMapping("/getImageCode" + WebConst.jsonSuffix)
    fun getImageCode(width: Int?, height: Int?) = ResultVO(this.getImageCodeBase64(width, height))

    private fun getImageCodeBase64(width: Int?, height: Int?): String {
        var widthTmp = width
        var heightTmp = height
        widthTmp = if (widthTmp == null) 220 else widthTmp
        heightTmp = if (heightTmp == null) 45 else heightTmp
        val imageCode = ImageCodeMathCalcUtil(widthTmp, heightTmp)
        var value = imageCode.getResult()
        value = MD5Util.getMD5(value)
        CookieUtil.setCookie(response, WebKeyEnum.cookieImageCodeV.shortName, value, WebConst.cookieImageCodeVInvalidTime)
        return imageCode.writeBASE64()
    }
}