package com.qiqinote

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.ComponentScan

/**
 * Created by vanki on 2018/1/17 21:46.
 */
@ServletComponentScan
@SpringBootApplication
@ComponentScan(basePackages = [
    "com.qiqinote.dao",
    "com.qiqinote.service",
    "com.qiqinote.controller",
    "com.qiqinote.configuration",
    "com.qiqinote.exception",
    "com.qiqinote.interceptor",
    "com.qiqinote.filter"
])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
