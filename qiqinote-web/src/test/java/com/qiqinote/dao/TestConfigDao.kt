package com.qiqinote.dao

import com.alibaba.fastjson.JSON
import com.qiqinote.dao.ConfigDao
import com.qiqinote.po.Config
import com.qiqinote.po.fieldjson.ConfigNoteTree
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by vanki on 2018/2/5 17:14.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TestConfigDao {
    @Autowired
    private lateinit var configDao: ConfigDao

    @Test
    fun t1() {
        val config = Config()
        config.userId = 1
//        config.noteTreeConfig = """{"treeCssCls":"col-xs-3","contentCssCls:"col-xs-9"}"""
        config.noteTreeConfig = JSON.parseObject(JSON.toJSONString(ConfigNoteTree("col-xs-3", "col-xs-9")), ConfigNoteTree::class.java)

        println(this.configDao.upsert(config))

        println(JSON.toJSONString(this.configDao.getByUserId(1)))
    }
}