package com.qiqinote.service.impl

import com.qiqinote.dao.ConfigDao
import com.qiqinote.po.Config
import com.qiqinote.service.ConfigService
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/9/3 17:47.
 */
@Service
class ConfigServiceImpl @Autowired constructor(
        private val configDao: ConfigDao
) : ConfigService {

    override fun upsert(config: Config): ResultVO<Unit> {
        val status = this.configDao.upsert(config)
        return if (status > 0) ResultVO() else ResultVO.buildFail()
    }

    override fun getByUserId(userId: Long): Config? {
        return this.configDao.getByUserId(userId)
    }
}