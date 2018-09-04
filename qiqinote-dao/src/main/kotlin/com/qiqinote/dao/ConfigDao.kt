package com.qiqinote.dao

import com.qiqinote.po.Config

/**
 * Created by vanki on 2018/9/3 16:47.
 */
interface ConfigDao {
    fun upsert(config: Config): Int

    fun getByUserId(userId: Long): Config?
}