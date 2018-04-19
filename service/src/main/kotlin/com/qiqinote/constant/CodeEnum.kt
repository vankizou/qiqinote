package com.qiqinote.constant

/**
 * Created by vanki on 2018/1/21 18:09.
 */
enum class CodeEnum(val code: Int, val msg: String) {
    /**
     * 公共, 100 - 999
     */
    SUCCESS(200, "成功"),

    FAIL(300, "失败"),
    NOT_LOGIN(301, "未登录"),
    NOT_LOGIN_HTML(302, "未登录, 跳转登录页"),

    PARAM_ERROR(400, "参数不符"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源未找到"),

    SYSTEM_ERROR(500, "系统异常"),

    /**
     * 用户相关
     */
    USER_EXISTS(1000, "用户名已存在"),
    USER_NOT_EXISTS(1001, "用户不存在"),
    USER_NAME_NOT_ALLOW(1002, "用户名不能是纯数字"),
    USER_ACCOUNT_OR_PWD_ERROR(1003, "帐号或密码错误"),

    FIND_PWD_QUESTIONS_NUM_NOT_ENOUGH(1004, "数量不足"),
    FIND_PWD_QUESTIONS_ANSWER_ERROR(1005, "答案错误"),

    /**
     * 图片相关
     */
    IMAGE_NOT_FOUNT(1100, "未找到图片资源"),
    IMAGE_UPLOAD_TOO_MANY(1101, "图片上传数量过多"),
    IMAGE_CODE_ERROR(1102, "验证码错误"),

    /**
     * 笔记相关
     */
    PWD_ERROR(1120, "密码错误"),
    NOTE_DOWNLOAD_FAIL(1121, "导出失败, 笔记内容为空"),
    NOTE_TITLE_LEN_ERROR(1122, "标题长度不符"),
}