package com.qiqinote.util

import org.apache.commons.lang3.StringUtils

/**
 * Created by vanki on 2018/1/23 18:59.
 */
object StringUtil {
    private val numberReg = Regex("\\-?[0-9]+")

    fun isNumber(str: String?) = str?.matches(numberReg) ?: false

    fun isEmpty(str: String?) = str == null || str.isEmpty()

    fun isNotEmpty(str: String?) = !isEmpty(str)

    fun isBlank(str: String?) = StringUtils.isBlank(str)

    fun isNotBlank(str: String?) = StringUtils.isNotBlank(str)

    fun isAnyBlank(vararg str: String?) = StringUtils.isAnyBlank(*str)

    /**
     * xss数据更改
     *
     * @param value
     *
     * @return
     */
    fun stripXSS(value: String?) = value?.replace("<".toRegex(), "&lt;")?.replace(">".toRegex(), "&gt;")

    @JvmStatic
    fun main(args: Array<String>) {
        println(isAnyBlank("ss", "123"))
    }
}
