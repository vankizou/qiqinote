package com.qiqinote.filter

import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

/**
 * Created by vanki on 2018/3/30 10:13.
 */
@WebFilter(filterName = "xssFilter", urlPatterns = ["/*"])
class XssFilter : Filter {
    override fun init(p0: FilterConfig) {
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        chain.doFilter(XSSRequestWrapper(request as HttpServletRequest), response)
    }

    override fun destroy() {
    }
}