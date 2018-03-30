package com.qiqinote.filter

import com.qiqinote.util.StringUtil
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * Created by vanki on 2018/3/30 10:12.
 */
class XSSRequestWrapper constructor(
        private val request: HttpServletRequest
) : HttpServletRequestWrapper(request) {
    private val excludeList = mutableListOf<String>()

    init {
        excludeList.add("noteDetailList[0].content")
    }

    override fun getParameterValues(parameter: String): Array<String?>? {
        val values = super.getParameterValues(parameter)
        if (values == null || excludeList.contains(parameter)) return values

        val count = values.size
        val encodedValues = arrayOfNulls<String>(count)
        for (i in 0 until count) {
            encodedValues[i] = StringUtil.stripXSS(values[i])
        }
        return encodedValues
    }

    override fun getParameter(parameter: String): String? {
        val value = super.getParameter(parameter)
        return if (value == null || excludeList.contains(parameter)) value else StringUtil.stripXSS(value)
    }

    override fun getHeader(name: String): String? {
        val value = super.getHeader(name)
        return if (value == null || excludeList.contains(name)) value else StringUtil.stripXSS(value)
    }
}