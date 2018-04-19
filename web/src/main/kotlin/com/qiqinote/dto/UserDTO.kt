package com.qiqinote.dto

import com.qiqinote.po.User
import com.qiqinote.vo.UserSimpleVO

/**
 * Created by vanki on 2018/1/24 17:24.
 */
class UserDTO() {
    var id: Long? = null
    var name: String? = null
    var alias: String? = null
    var gender: Int? = null
    var motto: String? = null
    var avatarInfo: PictureDTO? = null

    constructor(user: UserSimpleVO) : this() {
        this.id = user.id
        this.name = user.name
        this.alias = user.alias
        this.gender = user.gender
        this.motto = user.motto
    }

    constructor(user: User) : this() {
        this.id = user.id
        this.name = user.name
        this.alias = user.alias
        this.gender = user.gender
        this.motto = user.motto
    }
}