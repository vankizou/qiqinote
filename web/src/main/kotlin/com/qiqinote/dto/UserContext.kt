package com.qiqinote.dto

import com.qiqinote.po.Picture
import com.qiqinote.vo.UserSimpleVO

/**
 * Created by vanki on 2018/1/24 17:10.
 */
class UserContext {
    lateinit var user: UserDTO

    fun setAvatarInfo(avatarInfo: Picture?): UserContext {
        avatarInfo?.let {
            this.user.avatarInfo = PictureDTO(it)
        }
        return this
    }

    companion object {
        fun build(user: UserSimpleVO, avatarInfo: Picture?): UserContext {
            val uc = UserContext()
            uc.user = UserDTO(user)
            return uc.setAvatarInfo(avatarInfo)
        }
    }
}