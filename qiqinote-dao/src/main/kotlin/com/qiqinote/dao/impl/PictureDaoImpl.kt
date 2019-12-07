package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.PictureDao
import com.qiqinote.po.Picture
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by vanki on 2018/3/29 14:24.
 */
@Repository
class PictureDaoImpl @Autowired constructor(
        private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : PictureDao {
    private val rowMapper = BeanPropertyRowMapper(Picture::class.java)

    override fun insert(picture: Picture): Long {
        val paramMap = mutableMapOf<String, Any?>()
        paramMap["uuid"] = picture.uuid
        paramMap["user_id"] = picture.userId
        paramMap["name"] = picture.name
        paramMap["path"] = picture.path
        paramMap["width"] = picture.width
        paramMap["height"] = picture.height
        paramMap["size"] = picture.size
        paramMap["type"] = picture.type
        paramMap["use_type"] = picture.useType
        paramMap["create_datetime"] = Date()
        paramMap["del"] = DBConst.falseVal

        val key = GeneratedKeyHolder()
        val status = this.namedParameterJdbcTemplate
                .update(NamedSQLUtil.getInsertSQL(Picture::class, paramMap), MapSqlParameterSource(paramMap), key)
        return if (status > 0) key.key!!.toLong() else 0
    }

    override fun getById(id: Long): Picture? {
        val paramMap = mapOf("id" to id, "del" to DBConst.falseVal)
        val list = this.namedParameterJdbcTemplate.query(NamedSQLUtil.getSelectSQL(Picture::class, paramMap), paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun list(userId: Long, useType: Int, page: Int, row: Int): List<Picture> {
        val paramMap = mapOf("user_id" to userId, "use_type" to useType, "del" to DBConst.falseVal)
        val sql = NamedSQLUtil.getSelectSQL(Picture::class, paramMap)
        if (page < 1 || row <= 0) {
            return listOf()
        }
        val start = (page - 1) * row
        return this.namedParameterJdbcTemplate
                .query("$sql ORDER BY ID DESC LIMIT $start,$row", paramMap, rowMapper)
    }
}