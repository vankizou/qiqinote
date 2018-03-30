package com.qiqinote.constant

/**
 * Created by vanki on 2018/1/20 19:06.
 */
object DBConst {
    val falseVal = 0
    val trueVal = 1
    val defaultParentId = -1L
    val firstSequence = 0

    object Picture {
        val useTypeNote = 1       /* 笔记 */
        val useTypeAvatar = 2     /* 头像 */
    }

    object Note {
        val pathLink = "_"

        val typeNormal = 1      /* 笔记类型. 普通 */
        val typeMarkdown = 2    /* 笔记类型. markdown */

        val secretOpen = 0      /* 私密状态. 公开访问 */
        val secretPwd = 1       /* 私密状态. 密码访问 */
        val secretClose = 2     /* 私密状态. 私密访问 */
        val secretLink = 3      /* 私密状态. 链接访问 */

        val statusExamine = -1  /* 文章状态. 待审核 */
        val statusNoPass = 0    /* 文章状态. 不通过 */
        val statusPass = 1      /* 文章状态. 通过 */
    }

    object NoteDetail {
        val typeNormal = Note.typeNormal        /* 笔记类型. 普通 */
        val typeMarkdown = Note.typeMarkdown    /* 笔记类型. markdown */
    }

    object User {
        val genderSecret = 0    /* 性别. 保密 */
        val genderMale = 1      /* 性别. 男 */
        val genderFemale = 2    /* 性别. 女 */

        val statusClose = 0 /* 帐户状态. 禁用 */
        val statusOpen = 1  /* 帐户状态. 正常 */

        val registerOriginNone = 1      /* 注册来源. 无 */
        val registerOriginPhone = 2     /* 注册来源. 手机号 */
        val registerOriginWeixin = 3    /* 注册来源. 微信 */
        val registerOriginQq = 4        /* 注册来源. QQ */
        val registerOriginWeibo = 5     /* 注册来源. 微信 */
    }

    object UserLoginRecord {
        val originAutoLogin = 0 /* 登录来源. 自动登录 */
        val originNone = 1      /* 登录来源. 无 */
        val originPhone = 2     /* 登录来源. 手机号 */
        val originWeixin = 3    /* 登录来源. 微信 */
        val originQq = 4        /* 登录来源. QQ */
        val originWeibo = 5     /* 登录来源. 微信 */
    }
}