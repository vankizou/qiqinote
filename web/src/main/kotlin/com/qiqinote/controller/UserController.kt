package com.qiqinote.controller

import com.qiqinote.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by vanki on 2018/3/13 17:35.
 */
@Controller
@RequestMapping("/user")
class UserController @Autowired constructor(
        private val userService: UserService
) : BaseController() {

}