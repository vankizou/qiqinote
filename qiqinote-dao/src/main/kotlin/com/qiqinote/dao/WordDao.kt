package com.qiqinote.dao

import com.qiqinote.po.Word

/**
 * Created by vanki on 2018/5/7 18:22.
 */
interface WordDao {
    fun insert(word: Word): Int

    fun random(): String
}