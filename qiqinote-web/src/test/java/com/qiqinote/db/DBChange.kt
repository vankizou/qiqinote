package com.qiqinote.db

import com.qiqinote.dao.NoteDao
import org.apache.coyote.http11.Constants.a
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by vanki on 2018/2/5 17:14.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class DBChange {
    @Autowired
    private lateinit var noteDao: NoteDao

    @Test
    fun t1() {
        this.noteDao.updatePath(-1, "-1")
    }
}