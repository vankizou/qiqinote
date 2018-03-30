package com.qiqinote.service

import com.qiqinote.dao.UserLoginRecordDao
import com.qiqinote.po.UserLoginRecord

/**
 * Created by vanki on 2018/1/25 16:36.
 */
interface UserLoginRecordService : BaseService<UserLoginRecordDao> {
    fun add(userLoginRecord: UserLoginRecord): Int
}