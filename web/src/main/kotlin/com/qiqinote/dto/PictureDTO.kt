package com.qiqinote.dto

import com.qiqinote.constant.WebConst
import com.qiqinote.po.Picture

/**
 * Created by vanki on 2018/1/24 17:18.
 */
class PictureDTO() {
    var uuid: String? = null
    var path: String? = null
        set(path) {
            field = WebConst.imagePrefix + path
        }

    constructor(picture: Picture) : this() {
        this.uuid = picture.uuid
        this.path = picture.path
    }
}