package com.qiqinote.service.impl

import com.alibaba.fastjson.JSON
import com.qiqinote.constant.*
import com.qiqinote.dao.EnvDao
import com.qiqinote.dao.UserDao
import com.qiqinote.dto.PictureDTO
import com.qiqinote.po.Picture
import com.qiqinote.po.User
import com.qiqinote.po.UserLoginRecord
import com.qiqinote.service.*
import com.qiqinote.util.*
import com.qiqinote.vo.ResultVO
import com.qiqinote.vo.UserContextVO
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.BoundValueOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by vanki on 2018/1/19 11:33.
 */
@Service
class UserServiceImpl @Autowired constructor(
        private val userDao: UserDao,
        private val envDao: EnvDao,
        private val pictureService: PictureService,
        private val userLoginRecordService: UserLoginRecordService,
        private val redisTemplate: StringRedisTemplate,
        private val wordService: WordService
) : UserService, AbstractBaseService() {

    override fun signIn(
            request: HttpServletRequest,
            response: HttpServletResponse,
            account: String,
            password: String,
            isRemember: Boolean?,
            origin: Int?
    ): UserContextVO? {
        val uc = this.getUserContextVO(request, response, account, password, false, isRemember) ?: return null

        val userLoginRecord = UserLoginRecord.buildRequestInfo(request)
        userLoginRecord.origin = origin
        userLoginRecord.userId = uc.id

        this.userLoginRecordService.add(userLoginRecord)

        return uc
    }

    override fun singOut(request: HttpServletRequest, response: HttpServletResponse) {
        val token = UserUtil.getLoginTokenFromCookie(request)
        if (token != null) {
            this.redisTemplate.delete(RedisKeyEnum.kvLoginToken_.buildVariableKey(token))
        }
        CookieUtil.deleteCookie(response, WebKeyEnum.cookieLoginToken.shortName)
        CookieUtil.deleteCookie(response, WebKeyEnum.cookieUserPassword.shortName)
        CookieUtil.deleteCookie(response, WebKeyEnum.cookieLastNoteView.shortName)
        request.session.invalidate()
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
        if (StringUtil.isNotBlank(user.name) && user.name != oldUser?.name) {
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
            if (StringUtil.isBlank(user.alias)) {
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
            if (oldUser.gender == DBConst.User.genderFemale || oldUser.gender == DBConst.User.genderMale) {
                user.gender = null
            }

            if (StringUtil.isNotEmpty(user.password)) {
                user.password = PasswordUtil.getEncPwd(user.password!!)
            }
            this.userDao.updateById(oldUser.id!!, user)
            return ResultVO(oldUser.id!!)
        }
    }

    override fun getByAccount(account: String): User? {
        val accounted = account.trim()
        val userId = account.toLongOrNull()
        return if (userId != null) {
            this.getById(userId)
        } else {
            this.getByName(accounted)
        }
    }

    override fun getById(id: Long): User? {
        return this.userDao.getById(id)
    }

    override fun getByName(name: String): User? {
        return this.userDao.getByName(name)
    }

    override fun getUserContextVO(
            request: HttpServletRequest,
            response: HttpServletResponse,
            randomMotto: Boolean?
    ): UserContextVO? {
        return this.getUserContextVO(
                request,
                response,
                null,
                null,
                randomMotto,
                null
        )
    }

    override fun getUserContextVO(
            request: HttpServletRequest,
            response: HttpServletResponse,
            account: String?,
            password: String?,
            randomMotto: Boolean?,
            remember: Boolean?
    ): UserContextVO? {
        /**
         * 通过token获取用户登录信息
         */
        var tokenTmp = UserUtil.getLoginTokenFromCookie(request)
        if (account == null || password == null) {
            /**
             * 有用户名和密码则重新登录
             */
            if (StringUtil.isNotBlank(tokenTmp)) {
                val ops = this.buildLoginTokenOps(tokenTmp!!)
                val ucCache = ops.get()
                val cacheResult = getCacheObj(ucCache, UserContextVO::class.java)
                if (cacheResult.left) {
                    if (cacheResult.right != null) {
                        // 刷新token时效
                        ops.expire(RedisKeyEnum.kvLoginToken_.time, RedisKeyEnum.kvLoginToken_.timeUnit)
                    }
                    return cacheResult.right
                }
            }
        }
        if (tokenTmp != null) {
            this.redisTemplate.delete(RedisKeyEnum.kvLoginToken_.buildVariableKey(tokenTmp))
            CookieUtil.deleteCookie(response, WebKeyEnum.cookieLoginToken.shortName)
        }

        /**
         * 有密码表示记住登录，则会自动登录
         */
        val pwdTmp = password ?: UserUtil.getUserPwdFromCookie(request) ?: return null
        val accountTmp = account ?: UserUtil.getUserAccountInCookie(request)
        if (StringUtil.isBlank(accountTmp)) {
            return null
        }
        val userTmp = this.getByAccount(accountTmp!!) ?: return null
        if (!checkPwd(userTmp.password ?: "", pwdTmp)) {
            return null
        }

        val uc = this.buildUserContext(userTmp, randomMotto)
        if (!uc.isOK()) {
            return null
        }

        /**
         * 写缓存、写cookie
         */
        UserUtil.setUserAccountInCookie(response, accountTmp)
        tokenTmp = UUIDUtil.getUUID()
        UserUtil.setLoginTokenInCookie(response, tokenTmp)
        this.buildLoginTokenOps(tokenTmp)
                .set(JSON.toJSONString(uc), RedisKeyEnum.kvLoginToken_.time, RedisKeyEnum.kvLoginToken_.timeUnit)
        if (remember != null) {
            if (remember) {
                UserUtil.setUserPwdInCookie(response, pwdTmp)
            } else {
                CookieUtil.deleteCookie(response, WebKeyEnum.cookieUserPassword.shortName)
            }
        }
        return uc
    }

    private fun checkPwd(dbPwd: String, inputPwd: String): Boolean {
        return dbPwd == PasswordUtil.getEncPwd(inputPwd)
    }

    override fun buildUserContext(user: User, randomMotto: Boolean?): UserContextVO {
        val uc = UserContextVO()
        uc.build(user)

        /**
         * 用户头像信息
         */
        var avatar: Picture? = null
        if (user.avatarId != null) {
            avatar = this.pictureService.getById(user.avatarId!!)
        }
        avatar =
                if (avatar == null) {
                    val defaultAvatarId = this.envDao.getVByK(EnvEnum.defaultAvatarId)?.trim()?.toLongOrNull()
                    if (defaultAvatarId == null) {
                        null
                    } else {
                        this.pictureService.getById(defaultAvatarId)
                    }
                } else {
                    avatar
                }
        if (avatar != null) {
            uc.build(PictureDTO(this.imageDomain, avatar))
        }
        /**
         * 添加自动格言
         */
        if (randomMotto != null && randomMotto && StringUtils.isEmpty(user.motto)) {
            user.motto = this.wordService.random()
        }
        return uc
    }

    private fun buildLoginTokenOps(token: String): BoundValueOperations<String, String> {
        return this.redisTemplate.boundValueOps(RedisKeyEnum.kvLoginToken_.buildVariableKey(token))
    }
}
