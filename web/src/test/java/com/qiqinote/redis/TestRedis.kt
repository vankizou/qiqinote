package com.qiqinote.redis

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.connection.RedisZSetCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by vanki on 2018/2/5 17:14.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TestRedis {
//    @Autowired
//    private lateinit var redisTemplate: RedisTemplate<String, Long>
    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    @Test
    fun t1() {
        println()
//        println(this.redisTemplate.boundValueOps("a").set("tttt"))
//        println(this.redisTemplate.boundValueOps("a").get())
        println()

    }

    @Test
    fun t2() {
        this.redisTemplate.delete("zset")
        val ops = this.redisTemplate.boundZSetOps("zset")
        ops.add("1", -1.toDouble())
        ops.add("2", -2.toDouble())
        ops.add("3", -3.toDouble())
        ops.add("4", -4.toDouble())
        ops.add("5", -5.toDouble())
        ops.add("6", -6.toDouble())
        ops.add("7", -7.toDouble())
        ops.add("8", -8.toDouble())
        ops.add("9", -9.toDouble())
        ops.add("10", -10.toDouble())

//        ops.add(1, -1.toDouble())
//        ops.add(2, -2.toDouble())
//        ops.add(3, -3.toDouble())
//        ops.add(4, -4.toDouble())
//        ops.add(5, -5.toDouble())
//        ops.add(6, -6.toDouble())
//        ops.add(7, -7.toDouble())
//        ops.add(8, -8.toDouble())
//        ops.add(9, -9.toDouble())
//        ops.add(10, -10.toDouble())

        /*ops.add(-1, -1.toDouble())
        ops.add(-2, -2.toDouble())
        ops.add(-3, -3.toDouble())
        ops.add(-4, -4.toDouble())
        ops.add(-5, -5.toDouble())
        ops.add(-6, -6.toDouble())
        ops.add(-7, -7.toDouble())
        ops.add(-8, -8.toDouble())
        ops.add(-9, -9.toDouble())
        ops.add(-10, -10.toDouble())*/

        println()
        println()

        println(ops.removeRangeByScore(-8.toDouble(), -7.toDouble()))
        println(ops.remove(*mutableListOf("4", "5", "8", "7").toTypedArray()))
        println(ops.removeRange(0, 2))
        println(ops.rangeByLex(RedisZSetCommands.Range.range()))
        println(ops.range(0, 1))
        println(ops.range(0, 2))
        println(ops.range(0, 9))

        val set = ops.range(0, 9)!!.toString()
        println(set.substring(1, set.length - 1))


        println(" === ")
        println(ops.zCard())
        println(ops.size())

        println()
        println()
    }
}