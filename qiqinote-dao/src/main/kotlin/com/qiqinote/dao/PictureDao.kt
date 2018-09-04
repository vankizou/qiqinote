package com.qiqinote.dao

import com.qiqinote.model.Page
import com.qiqinote.po.Picture

/**
 * Created by vanki on 2018/1/23 14:04.
 */
interface PictureDao {
    fun insert(picture: Picture): Long

    fun getById(id: Long): Picture?

    fun page(userId: Long, useType: Int, currPage: Int, pageSize: Int, navNum: Int): Page<Picture>
}