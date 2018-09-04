package com.zhugeio.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Created by vanki on 2018/6/26 18:22.
 */
object JacksonUtil {
    fun getMapper(): ObjectMapper {
        val jsonMapper = jacksonObjectMapper()

        /**
         * 解决问题：json转对象时，若json字段比对象字段多抛出com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException异常
         */
        jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return jsonMapper
    }
}