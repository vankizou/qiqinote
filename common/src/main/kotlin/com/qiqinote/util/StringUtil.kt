package com.qiqinote.util

/**
 * Created by vanki on 2018/1/23 18:59.
 */
object StringUtil {
    private val numberReg = Regex("\\-?[0-9]+")

    fun isNumber(str: String?) = str?.matches(numberReg) ?: false

    fun isEmpty(str: String?) = str == null || str.length == 0

    fun isNotEmpty(str: String?) = !isEmpty(str)

    /**
     * xss数据更改
     *
     * @param value
     *
     * @return
     */
    fun stripXSS(value: String?) = value?.replace("<".toRegex(), "&lt;")?.replace(">".toRegex(), "&gt;")
}
