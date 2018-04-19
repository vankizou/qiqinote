package com.qiqinote.controller

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.WebConst
import com.qiqinote.po.FindPwdQuestion
import com.qiqinote.po.User
import com.qiqinote.service.FindPwdQuestionService
import com.qiqinote.service.UserService
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by vanki on 2018/3/13 17:35.
 */
@Controller
@RequestMapping("/user")
class UserController @Autowired constructor(
        private val userService: UserService,
        private val findPwdQuestionService: FindPwdQuestionService
) : BaseController() {

    @PostMapping("/update${WebConst.needLoginJsonSuffix}")
    fun update(user: User): ResultVO<Any> {
        user.id = this.getLoginUserId()
        return ResultVO(this.userService.upsertUser(user))
    }

    @PostMapping("/updatePwdQuestion${WebConst.needLoginJsonSuffix}")
    fun updatePwdQuestion(questions: List<FindPwdQuestion>): ResultVO<List<FindPwdQuestion>> {
        if (questions.size != 3) return ResultVO(CodeEnum.PARAM_ERROR)
        return this.findPwdQuestionService.upsert(this.getLoginUserId(), questions)
    }


}