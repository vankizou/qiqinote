package com.qiqinote.service

import com.alibaba.fastjson.JSON
import com.qiqinote.constant.ServiceConst
import com.qiqinote.util.StringUtil
import org.apache.commons.lang3.tuple.Pair
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment

/**
 * @author vanki
 * @date 2019-11-24 14:38
 */
abstract class AbstractBaseService {
    protected val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    protected lateinit var env: Environment

    @Value("${'$'}{qiqinote.image.domain}")
    protected lateinit var imageDomain: String

    companion object {
        private val log2 = LoggerFactory.getLogger(AbstractBaseService::class.java)

        fun <T> getCacheObj(cache: String?, cls: Class<T>): Pair<Boolean, T?> {
            if (StringUtil.isBlank(cache)) {
                return Pair.of(false, null)
            }
            if (cache == ServiceConst.cacheNullValue) {
                return Pair.of(true, null)
            }
            return Pair.of(
                    true,
                    try {
                        JSON.parseObject(cache, cls)
                    } catch (e: Exception) {
                        log2.error("", e)
                        null
                    }
            )
        }
    }
}

