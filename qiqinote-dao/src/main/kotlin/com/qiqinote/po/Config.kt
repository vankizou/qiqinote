package com.qiqinote.po

import com.qiqinote.po.fieldjson.ConfigNoteTree
import java.util.*

/**
 * Created by vanki on 2018/9/3 16:44.
 */
class Config {
    var id: Long? = null

    var userId: Long? = null

    var noteTreeConfig: ConfigNoteTree? = null

    var createDatetime: Date? = null

    var updateDatetime: Date? = null
}