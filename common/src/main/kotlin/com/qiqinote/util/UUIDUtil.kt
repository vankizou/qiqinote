package com.qiqinote.util

import java.util.*

/**
 * Created by vanki on 2018/2/24 16:59.
 */
object UUIDUtil {
    fun getUUID(): String {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase()
    }
}