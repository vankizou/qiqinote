package com.qiqinote.po

import java.util.*

/**
 * Created by vanki on 2018/5/2 14:28.
 */
class Comment {
    var id: Long? = null

    var parentId: Long? = null

    var path: String? = null

    var type: Int? = null

    var targetId: String? = null

    var targetUserId: String? = null

    var fromUserId: Long? = null

    var toUserId: Long? = null

    var content: String? = null

    var subNum: Int? = null

    var createDatetime: Date? = null

    var isDel: Int? = null
}