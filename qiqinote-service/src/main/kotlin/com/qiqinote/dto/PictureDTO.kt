package com.qiqinote.dto

import com.qiqinote.po.Picture
import java.util.*

/**
 * Created by vanki on 2018/1/24 17:18.
 */
class PictureDTO() {
    var uuid: String? = null
    var path: String? = null
    var name: String? = null
    var width: Int? = null
    var height: Int? = null
    var type: String? = null
    var useType: Int? = null
    var createDatetime: Date? = null

    constructor(imageDomain: String, picture: Picture) : this() {
        this.uuid = picture.uuid
        this.path = imageDomain + picture.path
        this.name = picture.name
        this.width = picture.width
        this.height = picture.height
        this.type = picture.type
        this.useType = picture.useType
        this.createDatetime = picture.createDatetime
    }
}