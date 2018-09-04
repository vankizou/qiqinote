package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.CommentDao
import com.qiqinote.dto.TargetCommentDTO
import com.qiqinote.dto.UserUnreadCommentDTO
import com.qiqinote.po.Comment
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by vanki on 2018/5/2 18:06.
 */
@Repository
class CommentDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : CommentDao {
    private val rowMapper = BeanPropertyRowMapper(Comment::class.java)
    private val rowMapperUserCommentDTO = BeanPropertyRowMapper(UserUnreadCommentDTO::class.java)
    private val rowMapperTargetCommentDTO = BeanPropertyRowMapper(TargetCommentDTO::class.java)

    override fun create(comment: Comment): Long {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["root_id"] = comment.rootId ?: DBConst.defaultParentId
        paramMap["parent_id"] = comment.parentId ?: DBConst.defaultParentId
        paramMap["path"] = comment.path
        paramMap["type"] = comment.type
        paramMap["target_id"] = comment.targetId
        paramMap["target_uri"] = comment.targetUri ?: comment.targetId
        paramMap["target_user_id"] = comment.targetUserId
        paramMap["from_user_id"] = comment.fromUserId
        paramMap["to_user_id"] = comment.toUserId
        paramMap["content"] = comment.content
        paramMap["sub_num"] = comment.subNum ?: 0
        paramMap["create_datetime"] = Date()
        paramMap["del"] = DBConst.falseVal

        val sql = NamedSQLUtil.getInsertSQL(Comment::class, paramMap)
        val key = GeneratedKeyHolder()
        val status = this.namedParameterJdbcTemplate.update(sql, MapSqlParameterSource(paramMap), key)
        return if (status > 0) key.key!!.toLong() else 0
    }

    override fun updateSubNumByRootId(id: Long): Int {
        if (id == DBConst.defaultParentId) return 0

        val sql = """
            UPDATE comment AS c1 ,
            (
                SELECT
                    count(1) AS num
                FROM
                    comment
                WHERE
                    del = ${DBConst.falseVal}
                AND root_id = $id
            ) AS c2
            SET c1.sub_num = c2.num
            WHERE
                id = $id
            AND del = ${DBConst.falseVal}
        """.trimIndent()
        return this.namedParameterJdbcTemplate.update(sql, mapOf<String, Any>())
    }

    override fun delete(id: Long): Int {
        val comment = this.getById(id) ?: return 0
        val paramMap = mapOf("del" to DBConst.trueVal)

        val sql = NamedSQLUtil.getUpdateSQLWithoutCondition(Comment::class, paramMap)
        return this.namedParameterJdbcTemplate.update(sql + " WHERE del=${DBConst.falseVal} AND (id=$id OR path like '${comment.path + "_" + id}%')", paramMap)
    }

    override fun getById(id: Long): Comment? {
        val paramMap = mapOf("id" to id, "del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(Comment::class, paramMap)

        val list = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun countRoot(type: Int, targetId: Long): Int {
        val sql = "SELECT COUNT(1) FROM comment WHERE type=$type AND del=${DBConst.falseVal} AND target_id=$targetId AND root_id=${DBConst.defaultParentId}"
        return this.namedParameterJdbcTemplate.queryForObject(sql, MapSqlParameterSource(), Int::class.java) ?: 0
    }

    /**
     * 用户查看他人对自己的评论时返回的数据
     */
    override fun listOfUserUnreadCommentDTO(type: Int, ids: MutableList<Long>): MutableList<UserUnreadCommentDTO> {
        val condition: String = when (ids.size) {
            0 -> return Collections.emptyList()
            1 -> "=${ids[0]}"
            else -> {
                val idsStr = ids.toString()
                "IN (${idsStr.substring(1, idsStr.length - 1)})"
            }
        }
        return listOfUserUnreadCommentDTO(type, condition)
    }

    override fun listOfUserUnreadCommentDTO(type: Int, ids: MutableSet<String>): MutableList<UserUnreadCommentDTO> {
        val condition: String = when (ids.size) {
            0 -> return Collections.emptyList()
            1 -> "=${ids.first()}"
            else -> {
                val idsStr = ids.toString()
                "IN (${idsStr.substring(1, idsStr.length - 1)})"
            }
        }
        return listOfUserUnreadCommentDTO(type, condition)
    }

    private fun listOfUserUnreadCommentDTO(type: Int, condition: String): MutableList<UserUnreadCommentDTO> {

        /**
         * 评论类型对应获取的目标字段。如：type=笔记，获取title信息
         */
        val targetTableName: String
        val targetField: String
        when (type) {
            DBConst.Comment.typeNote -> {
                targetTableName = "note"
                targetField = ",t.title AS target_title"
            }
            else -> return Collections.emptyList()
        }
        val sql = """
                SELECT
                    c.id,c.root_id,c.parent_id,c.path,c.type,target_id,target_uri,target_user_id,from_user_id,to_user_id,content,sub_num,c.create_datetime,c.del
                    ,u1.name as target_user_name,u1.alias as target_user_alias
                    ,u2.name as from_user_name,u2.alias as from_user_alias
                    ,u3.name as to_user_name,u3.alias as to_user_alias
                    $targetField
                    ,p1.path AS from_user_avatar
                FROM comment AS c
                LEFT JOIN user AS u1 ON c.target_user_id = u1.id
                LEFT JOIN user AS u2 ON c.from_user_id = u2.id
                LEFT JOIN user AS u3 ON c.to_user_id = u3.id
                LEFT JOIN $targetTableName as t ON t.id=c.target_id
                LEFT JOIN picture AS p1 ON u2.avatar_id = p1.id
                WHERE
                c.del = ${DBConst.falseVal}
                AND u1.del = ${DBConst.falseVal}
                AND u2.del = ${DBConst.falseVal}
                AND u3.del = ${DBConst.falseVal}
                AND t.del = ${DBConst.falseVal}
                AND c.id $condition
                ORDER BY id DESC
            """.trimIndent()
        return this.namedParameterJdbcTemplate.query(sql, mapOf<String, Any>(), rowMapperUserCommentDTO)
    }

    /**
     * 在详情页查看评论的数据
     */
    override fun listOfTargetCommentDTO(type: Int, targetId: Long, rootId: Long, orderBy: String?, currPage: Int, pageSize: Int): MutableList<TargetCommentDTO> {
        val startRow = ((if (currPage < 1) 1 else currPage) - 1) * pageSize

        val sql = """
            SELECT
                c.id,c.root_id,parent_id,c.path,c.type,target_id,target_uri,target_user_id,from_user_id,to_user_id,content,sub_num,c.create_datetime,c.del
                ,u1.name AS from_user_name,u1.alias AS from_user_alias
                ,u2.name AS to_user_name,u2.alias AS to_user_alias
                ,p1.path AS from_user_avatar
                ,p2.path AS to_user_avatar
            FROM comment AS c
            LEFT JOIN user AS u1 ON c.from_user_id = u1.id
            LEFT JOIN user AS u2 ON c.to_user_id = u2.id
            LEFT JOIN picture AS p1 ON u1.avatar_id = p1.id
            LEFT JOIN picture AS p2 ON u2.avatar_id = p2.id
            WHERE
            u1.del = ${DBConst.falseVal}
            AND u2.del = ${DBConst.falseVal}
            AND c.del = ${DBConst.falseVal}
            AND c.type = $type
            AND c.target_id = $targetId
            AND c.root_id = $rootId

            ORDER BY ${orderBy ?: "id DESC"}

            LIMIT $startRow, $pageSize
        """.trimIndent()

        return this.namedParameterJdbcTemplate.query(sql, mapOf<String, Any>(), rowMapperTargetCommentDTO)
    }

    override fun listOfTargetCommentDTO(ids: List<Long>): MutableList<TargetCommentDTO> {
        val condition: String = when (ids.size) {
            0 -> return Collections.emptyList()
            1 -> "=${ids[0]}"
            else -> {
                val idsStr = ids.toString()
                "IN (${idsStr.substring(1, idsStr.length - 1)})"
            }
        }

        val sql = """
            SELECT
                c.id,c.root_id,parent_id,c.path,c.type,target_id,target_uri,target_user_id,from_user_id,to_user_id,content,sub_num,c.create_datetime,c.del
                ,u1.name AS from_user_name,u1.alias AS from_user_alias
                ,u2.name AS to_user_name,u2.alias AS to_user_alias
                ,p1.path AS from_user_avatar
            FROM comment AS c
            LEFT JOIN user AS u1 ON c.from_user_id = u1.id
            LEFT JOIN user AS u2 ON c.to_user_id = u2.id
            LEFT JOIN picture AS p1 ON u1.avatar_id = p1.id
            WHERE
            u1.del = ${DBConst.falseVal}
            AND u2.del = ${DBConst.falseVal}
            AND c.del = ${DBConst.falseVal}
            AND c.id $condition
        """.trimIndent()

        return this.namedParameterJdbcTemplate.query(sql, mapOf<String, Any>(), rowMapperTargetCommentDTO)
    }
}