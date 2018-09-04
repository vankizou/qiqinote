package com.qiqinote.service

import com.qiqinote.po.Config
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/9/3 17:47.
 */
interface ConfigService {
    fun upsert(config: Config): ResultVO<Unit>

    fun getByUserId(userId: Long): Config?
}