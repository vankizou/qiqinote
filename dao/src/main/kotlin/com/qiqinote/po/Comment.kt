package com.qiqinote.po

import java.util.*

/**
 * Created by vanki on 2018/5/2 14:28.
 */
open class Comment {
    var id: Long? = null

    var rootId: Long? = null

    var parentId: Long? = null

    var path: String? = null

    var type: Int? = null

    var targetId: Long? = null

    var targetUri: String? = null

    var targetUserId: Long? = null

    var fromUserId: Long? = null

    var toUserId: Long? = null

    var content: String? = null

    var subNum: Int? = null

    var createDatetime: Date? = null

    var isDel: Int? = null
}