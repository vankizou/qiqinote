package com.qiqinote.exception

import com.qiqinote.constant.CodeEnum
import com.qiqinote.vo.ResultVO

/**
 * Created by vanki on 2018/1/25 18:50.
 */
class QiqiNoteException : RuntimeException {
    var codeEnum: CodeEnum

    constructor(error: ResultVO<out Any?>) : super(error.toString()) {
        this.codeEnum = CodeEnum.getByCode(error.code)!!
    }

    constructor(codeEnum: CodeEnum) : super(ResultVO<Any>(codeEnum).toString()) {
        this.codeEnum = codeEnum
    }
}