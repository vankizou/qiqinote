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
class TestUserService {
    @Autowired
    private lateinit var userService: UserServiceImpl

    @Test
    fun t1() {
        val user = User()
        user.id = 4
        user.name="name3"
        user.password="12345"
//        user.alias = "hahhahahahah"
        println(jacksonObjectMapper().writeValueAsString(this.userService.upsertUser(user)))
    }
}