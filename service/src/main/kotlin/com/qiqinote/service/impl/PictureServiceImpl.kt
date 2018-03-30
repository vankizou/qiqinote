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

    override fun add(picture: Picture): Int {
        if (StringUtil.isEmpty(picture.uuid) || picture.userId == null || picture.useType == null) {
            return 0
        }
        return this.pictureDao.insert(picture)
    }

    override fun getById(id: Long) = this.pictureDao.getById(id)

    override fun page(userId: Long, useType: Int, currPage: Int, pageSize: Int, navNum: Int) = this.pictureDao.page(userId, useType, currPage, pageSize, navNum)
}