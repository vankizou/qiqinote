package com.qiqinote.controller

import com.qiqinote.anno.NeedLogin
import com.qiqinote.po.Config
import com.qiqinote.po.fieldjson.ConfigNoteTree
import com.qiqinote.service.ConfigService
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

/**
 * Created by vanki on 2018/9/3 17:49.
 */
@ApiIgnore
@RestController
@RequestMapping("/config")
class ConfigController @Autowired constructor(
        private val configService: ConfigService
) : BaseController() {

    /**
     * 树页页面比例
     */
    @NeedLogin
    @PostMapping("/upsertNoteTreeConfig")
    fun upsertNoteTreeConfig(treeCssCls: String, contentCssCls: String): ResultVO<Unit> {
        val config = Config()
        config.userId = this.getLoginUserId()
        config.noteTreeConfig = ConfigNoteTree(treeCssCls, contentCssCls)

        return this.configService.upsert(config)
    }
}