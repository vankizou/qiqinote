package com.qiqinote.vo

import com.qiqinote.po.Note

/**
 * Created by vanki on 2018/3/8 17:19.
 */
class NoteTreeVO {
    var note: Note? = null

    var subNoteVOList: MutableList<NoteTreeVO>? = null
}