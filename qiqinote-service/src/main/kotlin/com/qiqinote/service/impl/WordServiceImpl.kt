package com.qiqinote.service.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qiqinote.dao.WordDao
import com.qiqinote.po.Word
import com.qiqinote.service.WordService
import com.qiqinote.util.StringUtil
import com.qiqinote.util.asynchttp.AsyncHttpClientUtil
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
        val wordStr =
                try {
                    AsyncHttpClientUtil.doGet("https://api.hitokoto.cn/?encode=json")
                } catch (e: Exception) {
                    null
                }?.value?.responseBody
        var w: String? = null
        if (StringUtil.isNotBlank(wordStr)) {
            val wordNode = jacksonObjectMapper().readTree(wordStr)

            var type = wordNode.path("type").asText()
            val from = wordNode.path("from").asText()
            val word = wordNode.path("hitokoto").asText()

            val wordObj = Word()

            type = when (type?.trim()) {
                "a" -> "动漫"
                "b" -> "漫画"
                "c" -> "游戏"
                "d" -> "小说"
                "e" -> "原创"
                "f" -> "网络"
                else -> "其他"
            }

            wordObj.type = type
            wordObj.from = from
            wordObj.word = word
            this.wordDao.insert(wordObj)

            w = word
        }
        return if (StringUtil.isBlank(w)) {
            this.wordDao.random()
        } else {
            w!!.trim()
        }
    }
}