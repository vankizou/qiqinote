package com.qiqinote.service.impl

import com.qiqinote.dao.UserLoginRecordDao
import com.qiqinote.po.UserLoginRecord
import com.qiqinote.service.UserLoginRecordService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/1/25 16:37.
 */
@Service
class UserLoginRecordServiceImpl @Autowired constructor(
        private val userLoginRecordDao: UserLoginRecordDao
) : UserLoginRecordService {
    override fun add(userLoginRecord: UserLoginRecord): Int {
        return this.userLoginRecordDao.insert(userLoginRecord)
    }
}