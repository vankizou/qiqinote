package com.qiqinote.dto

import com.qiqinote.po.Comment

/**
 * 未读评论对应数据
 *
 * Created by vanki on 2018/5/22 16:05.
 */
class UserUnreadCommentDTO : Comment() {
    var targetTitle: String? = null

    var targetUserName: String? = null

    var targetUserAlias: String? = null

    var fromUserName: String? = null

    var fromUserAlias: String? = null

    var fromUserAvatar: String? = null

    var toUserName: String? = null

    var toUserAlias: String? = null
}