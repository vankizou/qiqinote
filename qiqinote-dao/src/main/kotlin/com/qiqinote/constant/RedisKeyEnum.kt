package com.qiqinote.constant

import java.util.concurrent.TimeUnit

/**
 * Created by vanki on 2018/3/12 12:12.
 */
enum class RedisKeyEnum(val timeUnit: TimeUnit, val time: Long, desc: String) {

    kvLoginToken_(TimeUnit.MINUTES, 30, "登录token"),

    sOpenedNoteId_(TimeUnit.MINUTES, 0, "用户打开了的笔记树目录，#_userId -> [noteId]"),

    kvNoteTreePage_(TimeUnit.HOURS, 2, "笔记树");


    fun buildVariableKey(vararg parts: Any?): String {
        val keySB = StringBuffer(128)
        keySB.append(this.name)

        var flag = false
        parts.forEach {
            if (!flag) {
                flag = true
            }
            keySB.append((it?.toString()) ?: "").append("_")
        }
        return if (flag) keySB.substring(0, keySB.length - 1) else keySB.substring(0, keySB.length)
    }
}