package com.qiqinote.constant

/**
 * Created by vanki on 2018/3/12 12:12.
 */
enum class RedisKeyEnum {

    sOpenedNoteId_

    ;

    private val time: Int?  // 用户打开了的笔记树目录，#_userId -> [noteId]

    constructor() {
        this.time = null
    }

    constructor(time: Int) {
        this.time = time
    }
}