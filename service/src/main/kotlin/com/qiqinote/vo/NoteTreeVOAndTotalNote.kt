package com.qiqinote.vo

/**
 * Created by vanki on 2018/4/4 18:36.
 */
data class NoteTreeVOAndTotalNote(
        val notes: MutableList<NoteTreeVO>,
        var total: Int = 0
)