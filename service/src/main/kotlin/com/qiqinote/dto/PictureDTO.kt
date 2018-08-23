package com.qiqinote.dto

import com.qiqinote.po.Picture

/**
 * Created by vanki on 2018/1/24 17:18.
 */
class PictureDTO {
    var uuid: String? = null
    var path: String? = null
    var name: String? = null

    constructor(imageDomain: String, picture: Picture) {
        this.uuid = picture.uuid
        this.path = imageDomain + picture.path
        this.name = picture.name
    }
}