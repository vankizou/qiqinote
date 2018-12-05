package com.qiqinote.db

import com.qiqinote.dao.NoteDao
import com.qiqinote.po.Note
import org.apache.coyote.http11.Constants.a
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by vanki on 2018/2/5 17:14.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class DBChange {
    @Autowired
    private lateinit var noteDao: NoteDao
    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Test
    fun t1() {
        this.noteDao.updatePath(-1, "-1")
    }

    @Test
    fun t2() {
        val list = namedParameterJdbcTemplate.query(
                "select id, user_id from note where del=0",
                BeanPropertyRowMapper(Note::class.java)
        )

        list.forEach {
            this.noteDao.updateNoteNum(it.userId!!, it.id!!)
        }
    }
}