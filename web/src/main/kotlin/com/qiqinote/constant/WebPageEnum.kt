package com.qiqinote.constant

/**
 * Created by vanki on 2018/2/26 11:18.
 */
enum class WebPageEnum(val url: String) {
    _404("common/404"),
    _500("common/500"),

    login("index/login"),
    index("index/index"),

    note_list("note/list"),
    note_edit("note/edit"),
    note_view("note/view"),

    user_setting("user/setting")
}