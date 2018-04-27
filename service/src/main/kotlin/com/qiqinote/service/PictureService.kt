package com.qiqinote.service

import com.qiqinote.dao.PictureDao
import com.qiqinote.model.Page
import com.qiqinote.po.Picture

/**
 * Created by vanki on 2018/1/24 18:47.
 */
interface PictureService : BaseService<PictureDao> {
    fun add(picture: Picture): Long

    fun getById(id: Long): Picture?

    fun page(userId: Long, useType: Int, currPage: Int, pageSize: Int, navNum: Int = 10): Page<Picture>
}