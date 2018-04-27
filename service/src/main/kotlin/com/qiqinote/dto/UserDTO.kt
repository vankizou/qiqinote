package com.qiqinote.dto

import com.qiqinote.po.User
import com.qiqinote.util.EntityUtil
import java.util.*

/**
 * Created by vanki on 2018/1/24 17:24.
 */
class UserDTO {
    var id: Long? = null

    var name: String? = null

    var alias: String? = null

    var gender: Int? = null

    var motto: String? = null

    var phone: String? = null

    var email: String? = null

    var qq: String? = null

    var weixin: String? = null

    var weibo: String? = null

    var birthday: Date? = null

    var avatarId: Long? = null

    var avatar: PictureDTO? = null

    constructor(user: User) {
        EntityUtil.copyValOfDiffObj(this, user)
    }
}