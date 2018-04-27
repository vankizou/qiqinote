package com.qiqinote.util

import com.qiqinote.constant.ServiceConst
import com.qiqinote.constant.WebKeyEnum
import com.qiqinote.dto.PictureDTO
import com.qiqinote.dto.UserContext
import com.qiqinote.po.UserLoginRecord
import com.qiqinote.service.UserService
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/24 15:22.
 */
object UserUtil {
    private const val userRememberCookieDelim = ','

    fun setUserIdAndPwdInCookie(response: HttpServletResponse, userId: Long, pwd: String) {
        val date = Date()
        var value: String = StringBuilder()
                .append(date.time)
                .append(userRememberCookieDelim)
                .append(userId)
                .append(userRememberCookieDelim)
                .append(pwd)
                .append(userRememberCookieDelim)
                .append(DateUtil.formatDatetime(date))
                .toString()
        value = PasswordUtil.getEncPwd(value)
        CookieUtil.setCookie(response, WebKeyEnum.cookieRememberUser.shortName, value)
    }

    fun getUserIdAndPwdByCookie(request: HttpServletRequest): Array<String>? {
        var value: String? = CookieUtil.getCookie(request, WebKeyEnum.cookieRememberUser.shortName)
        if (StringUtil.isEmpty(value)) return null

        value = PasswordUtil.getDecPwd(value?.trim() ?: "")
        val loginInfos = value.split(userRememberCookieDelim)
        if (loginInfos.isEmpty() || loginInfos.size < 4) return null
        return arrayOf(loginInfos[1], loginInfos[2])
    }

    fun setUCInSession(request: HttpServletRequest, uc: UserContext) = request.getSession().setAttribute(WebKeyEnum.sessionUserContext.shortName, uc)

    fun getUCBySession(request: HttpServletRequest) = request.getSession().getAttribute(WebKeyEnum.sessionUserContext.shortName) as? UserContext

    fun signIn(request: HttpServletRequest, response: HttpServletResponse, userService: UserService,
               account: String, password: String, isRemember: Int, origin: Int?, imageDomain: String): Boolean {
        var userLoginRecord: UserLoginRecord? = null

        origin?.let {
            userLoginRecord = UserLoginRecord.buildRequestInfo(request)
            userLoginRecord!!.origin = origin
        }

        val resultVO = userService.preSignIn(account, password, userLoginRecord)
        val ucVO = resultVO.data
        if (ucVO?.user == null) return false

        var avatar: PictureDTO? = null
        ucVO.avatar?.let {
            avatar = PictureDTO(imageDomain, it)
        }

        UserUtil.setUCInSession(request, UserContext(ucVO.user!!, avatar))

        if (isRemember == ServiceConst.trueVal && ucVO.user?.id != null && password != null) {
            UserUtil.setUserIdAndPwdInCookie(response, ucVO.user?.id!!, password)
        }
        return true
    }
}