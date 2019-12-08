package com.qiqinote.dao.impl

import com.qiqinote.dao.WordDao
import com.qiqinote.po.Word
import com.qiqinote.util.StringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

/**
 * Created by vanki on 2018/5/7 18:23.
 */
@Repository
class WordDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : WordDao {
    private val rowMapper = BeanPropertyRowMapper(Word::class.java)

    override fun insert(word: Word): Int {
        if (StringUtil.isBlank(word.type) ||
                StringUtil.isBlank(word.from) ||
                StringUtil.isBlank(word.word) ||
                word.word!!.length > 500) {
            return 0
        }
        if (this.exists(word.word!!)) {
            return 0
        }

        val paramMap = mapOf("word" to word.word!!.trim(), "from" to word.from!!.trim(), "type" to word.type?.trim())
        val sql = "INSERT INTO word(`type`, `from`, `word`) VALUES(:type, :from, :word)"

        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun random(): String {
        val sql = "SELECT word from word ORDER BY RAND() LIMIT 1"

        val list = this.namedParameterJdbcTemplate.query(sql, rowMapper)
        return if (list.isEmpty()) "每天进步一点点！" else list[0].word!!
    }

    private fun exists(word: String): Boolean {
        val paramMap = mapOf("word" to word)
        val sql = "SELECT COUNT(1) FROM word WHERE word=:word"
        val count = this.namedParameterJdbcTemplate.queryForObject(sql, paramMap, Long::class.java) ?: 0
        return count > 0
    }
}