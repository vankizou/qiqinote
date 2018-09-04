package com.qiqinote.dto

import com.qiqinote.po.Comment

/**
 * 详情页查看的评论数据
 *
 * Created by vanki on 2018/5/22 17:01.
 */
class TargetCommentDTO : Comment() {
    var fromUserName: String? = null

    var fromUserAlias: String? = null

    var fromUserAvatar: String? = null

    var toUserName: String? = null

    var toUserAlias: String? = null

    var parent: TargetCommentDTO? = null
}