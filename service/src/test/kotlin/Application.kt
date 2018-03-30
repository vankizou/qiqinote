package com.qiqinote

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource

/**
 * Created by vanki on 2018/1/17 21:46.
 */
@SpringBootApplication
@ComponentScan(basePackages = ["com.qiqinote.dao", "com.qiqinote.service", "com.qiqinote.configuration"])
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args);
}
