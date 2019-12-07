package com.qiqinote.vo

import com.qiqinote.dto.PictureDTO
import com.qiqinote.po.User
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by vanki on 2018/1/24 18:43.
 */
class UserContextVO {
    var id: Long? = null

    var avatarId: Long? = null

    var name: String? = null

    var alias: String? = null

    var gender: Int? = null

    var status: Int? = null

    var motto: String? = null

    var phone: String? = null

    var email: String? = null

    var qq: String? = null

    var weixin: String? = null

    var weibo: String? = null

    var birthday: Date? = null

    var description: String? = null

    var createDatetime: Date? = null

    var avatarInfo: PictureDTO? = null

    fun build(user: User) {
        this.id = user.id
        this.avatarId = user.avatarId
        this.name = user.name
        this.alias = user.alias
        this.gender = user.gender
        this.status = user.status
        this.motto = user.motto
        this.phone = user.phone
        this.email = user.email
        this.qq = user.qq
        this.weixin = user.weixin
        this.weibo = user.weibo
        this.birthday = user.birthday
        this.description = user.description
        this.createDatetime = user.createDatetime
    }

    fun build(avatar: PictureDTO) {
        this.avatarInfo = avatar
    }

    fun isOK(): Boolean {
        return this.id != null && StringUtils.isNotBlank(name)
    }
}