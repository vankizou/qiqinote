package com.qiqinote.service.impl

import com.qiqinote.dao.PictureDao
import com.qiqinote.po.Picture
import com.qiqinote.service.PictureService
import com.qiqinote.util.StringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/1/24 18:47.
 */
@Service
class PictureServiceImpl @Autowired constructor(
        private val pictureDao: PictureDao
) : PictureService {
    override fun add(picture: Picture): Long {
        if (StringUtil.isEmpty(picture.uuid) || picture.userId == null || picture.useType == null) {
            return 0
        }
        return this.pictureDao.insert(picture)
    }

    override fun getById(id: Long): Picture? {
        return this.pictureDao.getById(id)
    }

    override fun list(userId: Long, useType: Int, page: Int, row: Int): List<Picture> {
        return this.pictureDao.list(userId, useType, page, row)
    }

}