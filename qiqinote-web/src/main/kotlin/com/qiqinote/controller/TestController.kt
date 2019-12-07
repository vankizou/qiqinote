package com.qiqinote.controller

import com.qiqinote.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

/**
 * Created by vanki on 2018/1/17 22:43.
 */
@ApiIgnore
@RestController
@RequestMapping("/test")
class TestController @Autowired constructor(val userDao: UserDao) {

    @ResponseBody
    @GetMapping("/t1.json")
    fun test1() = "helloworld!!!"

    @RequestMapping("/page/{page}.html")
    fun test2(@PathVariable("page") page: String): String {
        println(page.replace('-', '/'))
        return page.replace('-', '/')
    }
}