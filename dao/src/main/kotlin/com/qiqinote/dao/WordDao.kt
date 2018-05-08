package com.qiqinote.dao

/**
 * Created by vanki on 2018/5/7 18:22.
 */
interface WordDao {
    fun insert(word: String): Int

    fun random(): String
}