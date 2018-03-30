package com.qiqinote.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qiqinote.po.User
import com.qiqinote.service.impl.UserServiceImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by vanki on 2018/1/19 11:34.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TestNoteService {
    @Autowired
    private lateinit var noteService: NoteService

    @Test
    fun t1() {
    }
}