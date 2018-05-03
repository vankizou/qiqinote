package com.qiqinote.service.impl

import com.qiqinote.dao.CommentDao
import com.qiqinote.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/5/3 15:50.
 */
@Service
class CommentServiceImpl @Autowired constructor(
        private val commentDao: CommentDao
) : CommentService {

}