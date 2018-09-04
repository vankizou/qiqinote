package com.qiqinote.dao

import com.qiqinote.po.User

/**
 * Created by vanki on 2018/1/23 14:04.
 */
interface UserDao {
    fun insert(user: User): Long

    fun updateById(id: Long, user: User): Int

    fun countByName(name: String): Int

    fun getById(id: Long): User?

    fun getByName(name: String): User?
}