package com.qiqinote.constant

/**
 * Created by vanki on 2018/1/24 14:54.
 */
enum class WebKeyEnum(val shortName: String) {
    /**
     * cookie存储登录的token
     */
    cookieLoginToken("note_lt"),

    /**
     * cookie存储保存登录的信息
     */
    cookieUserAccount("note_ua"),

    /**
     * cookie选择记录密码时保存密码
     */
    cookieUserPassword("note_up"),

    /**
     * cookie存储图片验证相关信息
     */
    cookieImageCodeValue("note_icv"),

    /**
     * 保存上一次打开的笔记（每个笔记打开一次会添加一次view数，防刷）
     */
    cookieLastNoteView("note_lnv"),

}