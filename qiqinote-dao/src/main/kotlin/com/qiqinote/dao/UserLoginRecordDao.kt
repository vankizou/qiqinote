package com.qiqinote.dao

import com.qiqinote.po.UserLoginRecord

/**
 * Created by vanki on 2018/1/25 16:37.
 */
interface UserLoginRecordDao {
    fun insert(userLoginRecord: UserLoginRecord): Int
}