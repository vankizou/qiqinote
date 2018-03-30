package com.qiqinote.util

import com.qiqinote.constant.WebKeyEnum
import com.qiqinote.dto.UserContext
import com.qiqinote.po.UserLoginRecord
import com.qiqinote.service.UserService
import com.qiqinote.vo.ResultVO
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/24 15:22.
 */
object UserUtil {
    private const val userRememberCookieDelim = ','

    fun setUserIdInCookie(response: HttpServletResponse, userId: Long?) {
        if (userId == null) return
        var value: String = StringBuilder()
                .append(userId)
                .append(userRememberCookieDelim)
                .append(DateUtil.formatDatetime(Date()))
                .toString()
        value = PasswordUtil.getEncPwd(value)
        CookieUtil.setCookie(response, WebKeyEnum.cookieRememberUser.shortName, value)
    }

    fun getUserIdByCookie(request: HttpServletRequest): Long? {
        var value: String? = CookieUtil.getCookie(request, WebKeyEnum.cookieRememberUser.shortName)
        if (StringUtil.isEmpty(value)) return null

        value = PasswordUtil.getDecPwd(value?.trim() ?: "")
        return value.splitToSequence(userRememberCookieDelim).firstOrNull()?.toLongOrNull()
    }

    fun setUCInSession(request: HttpServletRequest, uc: UserContext) = request.getSession().setAttribute(WebKeyEnum.sessionUserContext.shortName, uc)

    fun getUCBySession(request: HttpServletRequest) = request.getSession().getAttribute(WebKeyEnum.sessionUserContext.shortName) as? UserContext

    fun signIn(request: HttpServletRequest, response: HttpServletResponse, userService: UserService,
               account: String?, password: String? = null, isRemember: Int = 0, origin: Int): ResultVO<UserContext> {
        val resultVO = userService.preSignIn(account, password, isRemember, origin, UserLoginRecord.buildRequestInfo(request))
        val ucVO = resultVO.data
        if (ucVO?.user == null) return ResultVO(resultVO.code, resultVO.msg)

        val uc = UserContext.build(ucVO.user!!, ucVO.avatar)
        UserUtil.setUCInSession(request, uc)
        if (isRemember == 1) UserUtil.setUserIdInCookie(response, uc.user.id)

        return ResultVO(uc)
    }
}