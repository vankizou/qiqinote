package com.qiqinote.vo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qiqinote.constant.CodeEnum

/**
 * Created by vanki on 2018/1/21 18:06.
 */
class ResultVO<T> {
    var code: Int = 0
    var msg: String? = null
    var data: T? = null

    constructor() : this(CodeEnum.SUCCESS)

    constructor(data: T) : this(CodeEnum.SUCCESS, data)

    constructor(sc: CodeEnum) : this(sc.code, sc.msg)

    constructor(sc: CodeEnum, data: T) : this(sc.code, sc.msg, data)

    constructor(code: Int, msg: String?) : this(code, msg, null)

    constructor(code: Int, msg: String?, data: T?) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    fun isSuccess() = this.code == CodeEnum.SUCCESS.code

    override fun toString(): String {
        return jacksonObjectMapper().writeValueAsString(this)
    }
}
