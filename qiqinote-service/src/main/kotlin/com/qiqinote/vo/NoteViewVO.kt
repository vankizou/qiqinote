package com.qiqinote.vo

import com.qiqinote.constant.ServiceConst
import com.qiqinote.dto.UserDTO
import com.qiqinote.po.Note
import com.qiqinote.po.NoteDetail

/**
 * Created by vanki on 2018/3/8 17:18.
 */
class NoteViewVO {
    var note: Note? = null
    var noteDetails: List<NoteDetail>? = null
    var needPwd: Int = ServiceConst.falseVal

    /**
     * 详情页
     */
//    var createDatetimeStr: String? = null
//    var updateDatetimeStr: String? = null
    var parentNote: Note? = null
    var user: UserContextVO? = null
}