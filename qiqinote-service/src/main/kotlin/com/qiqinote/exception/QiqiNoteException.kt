package com.qiqinote.exception

import com.alibaba.fastjson.JSON
import com.qiqinote.constant.CodeEnum

/**
 * Created by vanki on 2018/1/25 18:50.
 */
class QiqiNoteException(
        val code: Int,
        val msg: String
) : RuntimeException(JSON.toJSONString(mapOf("code" to code, "msg" to msg))) {

    constructor(codeEnum: CodeEnum) : this(codeEnum.code, codeEnum.msg)

}