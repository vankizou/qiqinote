package com.qiqinote.vo

import com.alibaba.fastjson.JSON
import com.qiqinote.constant.CodeEnum

/**
 * Created by vanki on 2018/1/21 18:06.
 */
class ResultVO<T> {
    var code: Int = 0
    var msg: String? = null
    var data: T? = null

    constructor() : this(CodeEnum.SUCCESS, null)

    constructor(data: T) : this(CodeEnum.SUCCESS, data)

    constructor(sc: CodeEnum, data: T?) : this(sc.code, sc.msg, data)

    constructor(code: Int, msg: String?) : this(code, msg, null)

    constructor(code: Int, msg: String?, data: T?) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    fun isSuccess() = this.code == CodeEnum.SUCCESS.code

    override fun toString(): String {
        return JSON.toJSONString(this)
    }

    companion object {
        fun buildFail() = ResultVO<Unit>(CodeEnum.FAIL, null)

        fun buildParamError() = ResultVO<Unit>(CodeEnum.PARAM_ERROR, null)
    }
}
