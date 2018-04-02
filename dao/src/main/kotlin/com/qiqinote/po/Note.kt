package com.qiqinote.po

import java.util.*

/**
 * Created by vanki on 2018/1/18 17:41.
 */
class Note {
    var id: Long? = null

    var idLink: String? = null

    var parentId: Long? = null

    var path: String? = null

    var userId: Long? = null

    var type: Int? = null

    var noteNum: Int? = null

    var noteContentNum: Int? = null

    var secret: Int? = null

    var password: String? = null

    var title: String? = null

    var keyword: String? = null

    var sequence: Int? = null

    var description: String? = null

    var viewNum: Long? = null

    var digest: String? = null

    var author: String? = null

    var originUrl: String? = null

    var status: Int? = null

    var statusDescription: String? = null

    var updateDatetime: Date? = null;

    var createDatetime: Date? = null;

    var isDel: Int? = null
}