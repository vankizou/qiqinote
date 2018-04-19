package com.qiqinote.service.impl

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.dao.UserDao
import com.qiqinote.po.Picture
import com.qiqinote.po.User
import com.qiqinote.po.UserLoginRecord
import com.qiqinote.service.PictureService
import com.qiqinote.service.UserLoginRecordService
import com.qiqinote.service.UserService
import com.qiqinote.util.EntityUtil
import com.qiqinote.util.PasswordUtil
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.ResultVO
import com.qiqinote.vo.UserContextVO
import com.qiqinote.vo.UserSimpleVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by vanki on 2018/1/19 11:33.
 */
@Service
class UserServiceImpl @Autowired constructor(
        private val userDao: UserDao,
        private val pictureService: PictureService,
        private val userLoginRecordService: UserLoginRecordService
) : UserService {

    override fun preSignIn(account: String, password: String, userLoginRecord: UserLoginRecord): ResultVO<UserContextVO?> {
        val passworded = password
        if (StringUtil.isBlank(account) || StringUtil.isEmpty(passworded)) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }
        val user = this.getByAccount(account)
        if (user == null || (user.password != null && passworded != PasswordUtil.getDecPwd(user.password!!))) {
            return ResultVO(CodeEnum.USER_ACCOUNT_OR_PWD_ERROR)
        }
        val userId = user.id!!

        /**
         * 帐号校验成功
         */
        userLoginRecord.userId = userId
        this.userLoginRecordService.add(userLoginRecord)

        val ucVO = this.getUserContextVO(user)
        return ResultVO(ucVO)
    }

    /**
     * 添加用户或修改用户信息
     */
    override fun upsertUser(user: User): ResultVO<Long> {
        user.name = user.name?.trim()
        user.password = user.password

        if (StringUtil.isNumber(user.name)) {
            return ResultVO(CodeEnum.USER_NAME_NOT_ALLOW)
        }

        var oldUser: User? = null
        if (user.id != null) {
            oldUser = this.getById(user.id!!)
            if (oldUser == null) {
                return ResultVO(CodeEnum.USER_NOT_EXISTS)
            }
        }

        /**
         * 用户名是否存在
         */
        if (StringUtil.isNotEmpty(user.name) && user.name != oldUser?.name) {
            val countName = this.userDao.countByName(user.name!!)
            if (countName > 0) {
                return ResultVO(CodeEnum.USER_EXISTS)
            }
        }

        if (oldUser == null) {
            /**
             * 添加
             */
            if (StringUtil.isEmpty(user.name) || StringUtil.isEmpty(user.password)) {
                return ResultVO(CodeEnum.PARAM_ERROR)
            }

            /**
             * 设置默认值
             */
            if (StringUtil.isEmpty(user.alias)) {
                user.alias = user.name
            }
            user.gender = user.gender ?: DBConst.User.genderSecret
            user.registerOrigin = user.registerOrigin ?: DBConst.User.registerOriginNone
            user.createDatetime = Date()
            user.password = PasswordUtil.getEncPwd(user.password!!)
            user.status = DBConst.User.statusOpen
            val id = this.userDao.insert(user)
            return ResultVO(id)
        } else {
            /**
             * 更新
             */
            // 性别设置只能改一次
            oldUser.gender?.let { user.gender = null }

            if (StringUtil.isNotEmpty(user.password)) {
                user.password = PasswordUtil.getEncPwd(user.password!!)
            }
            this.userDao.updateById(oldUser.id!!, user)
            return ResultVO(oldUser.id!!)
        }
    }

    override fun getByAccount(account: String): User? {
        val accounted = account?.trim()
        var userId = account.toLongOrNull()
        return if (userId != null) {
            this.getById(userId)
        } else {
            this.getByName(accounted!!)
        }
    }

    override fun getById(id: Long) = this.userDao.getById(id)

    override fun getByName(name: String) = this.userDao.getByName(name)

    override fun getUserContextVO(user: User?, userId: Long?, name: String?): UserContextVO? {
        var userTmp = user
        if (userTmp == null && userId == null && StringUtil.isEmpty(name)) {
            return null
        }

        if (userTmp == null) {
            userTmp = this.getById(userId!!)
        }
        if (userTmp == null) {
            userTmp = this.getByName(name!!)
        }
        if (userTmp == null) {
            return null
        }

        var avatar: Picture? = null
        if (userTmp.avatarId != null) {
            avatar = this.pictureService.getById(userTmp.avatarId!!)
        }
        val ucVO = UserContextVO()
        ucVO.user = EntityUtil.copyValOfDiffObj(UserSimpleVO(), userTmp)
        ucVO.avatar = avatar
        return ucVO
    }

}
