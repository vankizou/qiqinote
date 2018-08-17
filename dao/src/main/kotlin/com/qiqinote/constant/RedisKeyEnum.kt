package com.qiqinote.constant

import java.util.concurrent.TimeUnit

/**
 * Created by vanki on 2018/3/12 12:12.
 */
enum class RedisKeyEnum(val timeUnit: TimeUnit, val time: Long, desc: String) {

    sOpenedNoteId_(TimeUnit.MINUTES, 0, "用户打开了的笔记树目录，#_userId -> [noteId]"),

    kvNoteTreePage_(TimeUnit.HOURS, 2, "笔记树")

}