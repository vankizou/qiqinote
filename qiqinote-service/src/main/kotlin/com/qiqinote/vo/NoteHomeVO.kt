package com.qiqinote.vo

import com.qiqinote.po.Note

/**
 * Created by vanki on 2018/3/8 17:19.
 */
class NoteHomeVO {
    var user: UserContextVO? = null

    var parentNote: Note? = null

    var note: Note? = null

    var noteContent: String? = null
}