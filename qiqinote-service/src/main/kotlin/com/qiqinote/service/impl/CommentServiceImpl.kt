package com.qiqinote.service.impl

import com.qiqinote.constant.CodeEnum
import com.qiqinote.constant.DBConst
import com.qiqinote.constant.ServiceConst
import com.qiqinote.consts.RedisKey
import com.qiqinote.dao.CommentDao
import com.qiqinote.dto.TargetCommentDTO
import com.qiqinote.dto.UserUnreadCommentDTO
import com.qiqinote.exception.QiqiNoteException
import com.qiqinote.model.Page
import com.qiqinote.po.Comment
import com.qiqinote.service.CommentService
import com.qiqinote.service.NoteService
import com.qiqinote.util.StringUtil
import com.qiqinote.vo.ResultVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by vanki on 2018/5/3 15:50.
 */
@Service
class CommentServiceImpl @Autowired constructor(
        private val redisTemplate: StringRedisTemplate,
        private val environment: Environment,
        private val commentDao: CommentDao,
        private val noteService: NoteService
) : CommentService {
    private val imageDomain = environment["qiqinote.image.domain"]

    override fun create(comment: Comment): ResultVO<Comment> {
        val type = comment.type
        val targetId = comment.targetId
        if (type == null || targetId == null || StringUtil.isBlank(comment.content) ||
                comment.fromUserId == null || comment.toUserId == null) {
            return ResultVO(CodeEnum.PARAM_ERROR)
        }
        this.buildTargetInfo(comment, type, targetId)

        comment.rootId = comment.rootId ?: DBConst.defaultParentId
        comment.parentId = comment.parentId ?: DBConst.defaultParentId
        comment.path = this.getPath(comment.rootId)

        comment.subNum = 0
        comment.createDatetime = Date()
        comment.del = DBConst.falseVal

        val statusOrId = this.commentDao.create(comment)
        if (statusOrId > 0) {
            this.commentDao.updateSubNumByRootId(comment.rootId!!)

            /**
             * 添加未读消息
             */
            if (comment.targetUserId != comment.fromUserId) {
                this.addUnread(comment.targetUserId!!, type, statusOrId)
            }
            if (comment.targetUserId != comment.toUserId) this.addUnread(comment.toUserId!!, type, statusOrId)

            val commentDTO = this.listOfTarget(type, comment.rootId!!, listOf(statusOrId), comment.fromUserId)
            return if (commentDTO.isEmpty()) ResultVO(CodeEnum.FAIL) else ResultVO(commentDTO[0])
        }
        return ResultVO(CodeEnum.FAIL)
    }

    override fun delete(loginUserId: Long, type: Int, id: Long): Int {
        val old = this.getById(id) ?: return 0

        if (old.fromUserId != loginUserId && loginUserId != ServiceConst.adminUserId) {
            throw QiqiNoteException(CodeEnum.FORBIDDEN)
        }
        val status = this.commentDao.delete(id)

        /**
         * 清除接收者或作者的未读消息
         */
        if (status > 0) {
            this.commentDao.updateSubNumByRootId(old.rootId!!)

            val idArr = arrayOf(id.toString())
            if (old.toUserId != null) this.removeUnread(old.toUserId!!, type, idArr)
            if (old.targetUserId != null) this.removeUnread(old.targetUserId!!, type, idArr)
        }

        return status
    }

    override fun getById(id: Long) = this.commentDao.getById(id)

    override fun countRoot(type: Int, targetId: Long) = this.commentDao.countRoot(type, targetId)

    override fun listOfTarget(type: Int, rootId: Long, ids: List<Long>, loginUserId: Long?): MutableList<TargetCommentDTO> {
        val resultList = this.commentDao.listOfTargetCommentDTO(ids)
        this.buildTargetParentCommentAndRemoveUnread(type, rootId, loginUserId, resultList)
        return resultList
    }

    override fun listOfTarget(type: Int, targetId: Long, rootId: Long, currPage: Int, pageSize: Int, loginUserId: Long?): MutableList<TargetCommentDTO> {
        val resultList = this.commentDao.listOfTargetCommentDTO(type, targetId, rootId, if (rootId == DBConst.defaultParentId) "id DESC" else "id ASC", currPage, pageSize)
        this.buildTargetParentCommentAndRemoveUnread(type, rootId, loginUserId, resultList)
        return resultList
    }

    private fun buildTargetParentCommentAndRemoveUnread(type: Int, rootId: Long, loginUserId: Long?, commentList: MutableList<TargetCommentDTO>) {
        if (commentList.isEmpty()) return

        val map = mutableMapOf<Long, TargetCommentDTO>()
        val parentIds = mutableListOf<Long>()
        commentList.forEach {
            if (it.id != null) map[it.id!!] = it
            if (it.parentId != null && it.parentId != DBConst.defaultParentId && it.parentId != rootId) parentIds.add(it.parentId!!)
            buildAvatarBasePath(it)
        }
        /**
         * 构建父评论
         */
        if (rootId != DBConst.defaultParentId) {
            val filteredParentIds = parentIds.filter { !map.containsKey(it) }
            // 数据库获取父数据
            val parents = this.commentDao.listOfTargetCommentDTO(filteredParentIds)
            if (parents.isNotEmpty()) {
                parents.forEach { if (it.id != null) map[it.id!!] = it }
            }

            // 组装父评论
            commentList.forEach {
                val parentId = it.parentId
                if (parentId != null && parentId != DBConst.defaultParentId && parentId != rootId) {
                    it.parent = map[parentId]
                    if (it.parent != null) {
                        buildAvatarBasePath(it.parent!!)
                    }
                }
            }
        }

        /**
         * 如果我是作者，清除本次获取的未读评论
         */
        if (loginUserId != null && loginUserId == commentList[0].targetUserId) {
            val unreadNum = this.unreadNum(loginUserId, type)
            if (unreadNum > 0) {
                this.removeUnread(loginUserId, type, map.keys.map { it.toString() }.toTypedArray())
            }
        }
    }

    private fun addUnread(userId: Long, type: Int, id: Long) = this.getRedisOpt(userId, type).add(id.toString(), -id.toDouble())

    private fun removeUnread(userId: Long, type: Int, ids: Array<String>) = this.getRedisOpt(userId, type).remove(*ids)

    override fun unreadNum(userId: Long, type: Int) = this.getRedisOpt(userId, type).size() ?: 0

    override fun pageOfUnread(userId: Long, type: Int, pageSize: Int): Page<UserUnreadCommentDTO> {
        val opt = this.getRedisOpt(userId, type)
        val totalRow = opt.size()?.toInt() ?: 0

        val result = Page<UserUnreadCommentDTO>(Page.firstPage, pageSize, totalRow)
        if (totalRow == 0) {
            result.data = Collections.emptyList()
            return result
        }

        val startIndex = 0L
        val lastIndex = (pageSize - 1).toLong()

        val ids = opt.range(startIndex, lastIndex) ?: return result
        opt.removeRange(startIndex, lastIndex)

        result.data = this.commentDao.listOfUserUnreadCommentDTO(type, ids)
        result.data.let { it.forEach { buildAvatarBasePath(it) } }
        return result
    }

    /**
     * 构建头像全路径
     */
    private fun buildAvatarBasePath(dto: TargetCommentDTO) {
        if (dto.fromUserAvatar != null && !dto.fromUserAvatar!!.startsWith(imageDomain!!)) {
            dto.fromUserAvatar = imageDomain + dto.fromUserAvatar
        }
        if (dto.parent?.fromUserAvatar != null && !dto.parent?.fromUserAvatar!!.startsWith(imageDomain!!)) {
            dto.fromUserAvatar = imageDomain + dto.fromUserAvatar
        }
    }

    /**
     * 构建头像全路径
     */
    private fun buildAvatarBasePath(dto: UserUnreadCommentDTO) {
        if (dto.fromUserAvatar != null) {
            dto.fromUserAvatar = imageDomain + dto.fromUserAvatar
        }
    }

    private fun getPath(rootId: Long?): String {
        if (rootId == null || rootId == DBConst.defaultParentId) return "${DBConst.defaultParentId}"

        val parent = this.getById(rootId) ?: throw QiqiNoteException(CodeEnum.NOT_FOUND)
        return parent.path + "_" + rootId
    }

    private fun buildTargetInfo(comment: Comment, type: Int, targetId: Long) {
        if (type == DBConst.Comment.typeNote) {
            val note = this.noteService.getByIdOrIdLink(targetId) ?: return

            if (note.secret == DBConst.Note.secretLink) {
                comment.targetUri = note.idLink
            } else {
                comment.targetUri = note.id.toString()
            }
            comment.targetUserId = note.userId
        }
    }

    private fun getRedisOpt(userId: Long, type: Int) = this.redisTemplate.boundZSetOps(RedisKey.ZSET_USER_UNREAD_COMMENT_.name + "${userId}_$type")
}