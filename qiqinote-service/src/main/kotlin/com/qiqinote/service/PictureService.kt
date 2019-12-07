package com.qiqinote.service

import com.qiqinote.dao.PictureDao
import com.qiqinote.po.Picture

/**
 * Created by vanki on 2018/1/24 18:47.
 */
interface PictureService : BaseService<PictureDao> {
    fun add(picture: Picture): Long

    fun getById(id: Long): Picture?

    fun list(userId: Long, useType: Int, page: Int, row: Int): List<Picture>
}