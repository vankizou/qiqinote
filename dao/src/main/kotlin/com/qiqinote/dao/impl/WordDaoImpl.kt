package com.qiqinote.dao.impl

import com.qiqinote.dao.WordDao
import com.qiqinote.po.Word
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

    override fun insert(word: String): Int {
        if (word.length > 500) return 0

        val paramMap = mapOf("word" to word)
        val sql = "INSERT IGNORE word(word) VALUES(:word)"

        return this.namedParameterJdbcTemplate.update(sql, paramMap)
    }

    override fun random(): String {
        val sql = "SELECT word from word ORDER BY RAND() LIMIT 1"

        val list = this.namedParameterJdbcTemplate.query(sql, rowMapper)
        return if (list.isEmpty()) "每天进步一点点！" else list[0].word!!
    }
}