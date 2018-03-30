package com.qiqinote.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by vanki on 2018/1/24 15:35.
 */
object DateUtil {
    private val patternDatetime = "yyyy-MM-dd HH:mm:ss"
    private val patternDate = "yyyy-MM-dd"
    private val sdfDatetime = SimpleDateFormat(patternDatetime)
    private val sdfDate = SimpleDateFormat(patternDate)

    fun parseDate(dateStr: String?): Date? {
        return sdfDate.parse(dateStr ?: return null)
    }

    fun parseDatetime(datetimeStr: String?): Date? {
        return sdfDatetime.parse(datetimeStr ?: return null)
    }

    fun formatDate(date: Date?): String? {
        return sdfDate.format(date ?: return null)
    }

    fun formatDatetime(date: Date?): String? {
        return sdfDatetime.format(date ?: return null)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(parseDate("2018-11-12 10:23:22"))
        println(parseDatetime("2018-11-12 10:23:22"))
        println(formatDate(Date()))
        println(formatDatetime(Date()))
    }
}