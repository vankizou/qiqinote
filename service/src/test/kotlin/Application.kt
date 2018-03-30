package com.qiqinote

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource

/**
 * Created by vanki on 2018/1/17 21:46.
 */
@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.qiqinote.repository", "com.qiqinote.service"))
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args);
}
