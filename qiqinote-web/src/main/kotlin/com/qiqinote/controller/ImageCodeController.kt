package com.qiqinote.controller

import com.qiqinote.constant.WebConst
import com.qiqinote.constant.WebKeyEnum
import com.qiqinote.util.CookieUtil
import com.qiqinote.util.MD5Util
import com.qiqinote.util.imagecode.ImageCodeMathCalcUtil
import com.qiqinote.vo.ResultVO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by vanki on 2018/2/26 14:06.
 */
@Api("图片验证码")
@RestController
@RequestMapping("/imagecode")
class ImageCodeController : BaseController() {

    @ApiOperation("获取图片验证码")
    @ApiImplicitParams(
            ApiImplicitParam(name = "width", value = "图片宽度，默认：220"),
            ApiImplicitParam(name = "height", value = "图片高度，默认：45")
    )
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
        CookieUtil.setCookie(response, WebKeyEnum.cookieImageCodeValue.shortName, value, WebConst.cookieImageCodeVInvalidTime)
        return imageCode.writeBASE64()
    }
}