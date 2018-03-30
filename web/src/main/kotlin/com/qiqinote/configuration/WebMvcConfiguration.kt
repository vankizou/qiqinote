package com.qiqinote.configuration

import com.qiqinote.interceptor.LoginInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Created by vanki on 2018/3/21 16:56.
 */
@Configuration
class WebMvcConfiguration @Autowired constructor(
        private var loginInterceptor: LoginInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterceptor)
    }
}