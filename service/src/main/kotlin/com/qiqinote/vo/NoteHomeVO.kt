package com.qiqinote.vo

import com.qiqinote.po.Note
import com.qiqinote.po.User

/**
 * Created by vanki on 2018/3/8 17:19.
 */
class NoteHomeVO {
    var user: UserSimpleVO? = null
    var parentNote: Note? = null
    var note: Note? = null
}