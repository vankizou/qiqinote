package com.qiqinote.service

import com.qiqinote.dao.UserDao
import com.qiqinote.po.User
import com.qiqinote.vo.ResultVO
import com.qiqinote.vo.UserContextVO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/20 20:09.
 */
interface UserService : BaseService<UserDao> {
    fun signIn(
            request: HttpServletRequest,
            response: HttpServletResponse,
            account: String,
            password: String,
            isRemember: Boolean?,
            origin: Int?
    ): UserContextVO?

    fun singOut(request: HttpServletRequest, response: HttpServletResponse)

    fun upsertUser(user: User): ResultVO<Long>

    fun getById(id: Long): User?

    fun getByName(name: String): User?

    fun getByAccount(account: String): User?

    fun getUserContextVO(request: HttpServletRequest, response: HttpServletResponse): UserContextVO?

    fun getUserContextVO(request: HttpServletRequest, response: HttpServletResponse, account: String?, password: String?, remember: Boolean? = null): UserContextVO?
}