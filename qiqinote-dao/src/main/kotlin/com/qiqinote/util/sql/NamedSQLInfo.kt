package com.qiqinote.util.sql

/**
 * Created by vanki on 2018/3/20 18:17.
 */
data class NamedSQLInfo(val sql: String, val paramMap: LinkedHashMap<String, Any?>)