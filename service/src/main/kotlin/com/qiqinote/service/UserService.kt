package com.qiqinote.service

import com.qiqinote.dao.UserDao
import com.qiqinote.po.User
import com.qiqinote.po.UserLoginRecord
import com.qiqinote.vo.UserContextVO
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/1/20 20:09.
 */
interface UserService : BaseService<UserDao> {
    fun upsertUser(user: User): ResultVO<Long>

    fun preSignIn(account: String, password: String, userLoginRecord: UserLoginRecord?): ResultVO<UserContextVO?>

    fun getById(id: Long): User?

    fun getByName(name: String): User?

    fun getByAccount(account: String): User?

    fun getByAccount(account: String, loginUser: User?): User?

    fun getUserContextVO(user: User? = null, userId: Long? = null, name: String? = null): UserContextVO?
}