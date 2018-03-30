package com.qiqinote.redis

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by vanki on 2018/2/5 17:14.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TestRedis {
    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate
//    private lateinit var redis: RedisTemplate<String, String>

    @Test
    fun t1() {
        println()
        println(this.stringRedisTemplate.boundValueOps("a").set("tttt"))
        println(this.stringRedisTemplate.boundValueOps("a").get())
        println()

    }
}