package com.qiqinote.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.http.HttpStatus
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger
import java.io.IOException


/**
 * Created by vanki on 2018/3/22 18:25.
 */
object HttpUtil {
    private val log = Logger.getLogger(HttpUtil::class.java)
    private var requestConfig: RequestConfig? = null

    init {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(5000).build();
    }

    fun doPost(url: String, param: String?): String? {
        val httpClient = HttpClients.createDefault()
        var result: String? = null
        val httpPost = HttpPost(url)
        httpPost.config = requestConfig
        try {
            if (param != null && !param.isEmpty()) {
                val entity = StringEntity(param, "UTF-8")
                entity.setContentEncoding("UTF-8")
                entity.setContentType("application/json")
                httpPost.entity = entity
            }
            val response = httpClient.execute(httpPost)
            if (response.statusLine.statusCode == HttpStatus.SC_OK) {
                response.entity?.let { result = EntityUtils.toString(response.entity, "UTF-8") }
            } else {
                log.error("POST请求失败, url: $url, response: ${jacksonObjectMapper().writeValueAsString(response)}")
            }
        } catch (e: IOException) {
            log.error("POST请求失败: $url", e)
        } finally {
            httpPost.releaseConnection()
        }
        return result
    }

    fun doGet(url: String): String? {
        var result: String? = null
        val client = HttpClients.createDefault()
        val request = HttpGet(url)
        request.config = requestConfig
        try {
            val response = client.execute(request)
            if (response.statusLine.statusCode == HttpStatus.SC_OK) {
                response.entity?.let { result = EntityUtils.toString(response.entity, "UTF-8") }
            } else {
                log.error("GET请求失败, url: $url, response: ${jacksonObjectMapper().writeValueAsString(response)}")
            }
        } catch (e: IOException) {
            log.error("GET请求失败: $url", e)
        } finally {
            request.releaseConnection()
        }
        return result
    }
}