package com.qiqinote.service.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qiqinote.dao.WordDao
import com.qiqinote.po.Word
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
        val wordStr = HttpUtil.doGet("http://api.hitokoto.cn")
        var w: String? = null
        wordStr?.let {
            val wordNode = jacksonObjectMapper().readTree(wordStr)

            val from = wordNode.path("from").asText()
            val word = wordNode.path("hitokoto").asText()

            val wordObj = Word()
            wordObj.from = from
            wordObj.word = word
            this.wordDao.insert(wordObj)

            w = word
        }
        return if (StringUtil.isBlank(w)) this.wordDao.random() else w!!
    }
}