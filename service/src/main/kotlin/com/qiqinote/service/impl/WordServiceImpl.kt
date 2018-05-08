package com.qiqinote.service.impl

import com.qiqinote.dao.WordDao
import com.qiqinote.service.WordService
import com.qiqinote.util.HttpUtil
import com.qiqinote.util.StringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by vanki on 2018/5/7 18:32.
 */
@Service
class WordServiceImpl @Autowired constructor(
        private val wordDao: WordDao
) : WordService {
    override fun random(): String {
        val word = HttpUtil.doGet("http://api.hitokoto.cn/?encode=text")
        if (StringUtil.isBlank(word)) {
            return this.wordDao.random()
        } else {
            this.wordDao.insert(word!!)
        }
        return word
    }
}