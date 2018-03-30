package com.qiqinote.exception

import com.qiqinote.constant.CodeEnum
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/1/25 18:50.
 */
class QiqiNoteException(val codeEnum: CodeEnum) : RuntimeException(ResultVO<Any>(codeEnum).toString())