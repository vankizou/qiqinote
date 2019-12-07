package com.qiqinote.dao

import com.qiqinote.constant.EnvEnum

/**
 * Created by vanki on 2019/11/30 15:16.
 */
interface EnvDao {
    fun getVByK(k: EnvEnum): String?
}