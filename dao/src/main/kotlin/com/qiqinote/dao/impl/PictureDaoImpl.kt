package com.qiqinote.dao.impl

import com.qiqinote.constant.DBConst
import com.qiqinote.dao.PictureDao
import com.qiqinote.model.Page
import com.qiqinote.po.Picture
import com.qiqinote.util.sql.NamedSQLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
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

    override fun insert(picture: Picture): Int {
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
        paramMap["is_del"] = DBConst.falseVal

        return this.namedParameterJdbcTemplate.update(NamedSQLUtil.getInsertSQL(Picture::class, paramMap), paramMap)
    }

    override fun getById(id: Long): Picture? {
        val paramMap = mapOf("id" to id, "is_del" to DBConst.falseVal)
        val list = this.namedParameterJdbcTemplate.query(NamedSQLUtil.getSelectSQL(Picture::class, paramMap), paramMap, rowMapper)
        return if (list.isEmpty()) null else list[0]
    }

    override fun page(userId: Long, useType: Int, currPage: Int, pageSize: Int, navNum: Int): Page<Picture> {
        val paramMap = mapOf("user_id" to userId, "use_type" to useType, "is_del" to DBConst.falseVal)

        val countSql = NamedSQLUtil.getSelectSQL(Picture::class, "COUNT(*)", paramMap)
        val total = this.namedParameterJdbcTemplate.queryForObject(countSql, paramMap, Int::class.java)

        val returnPage = Page<Picture>(currPage, pageSize, total ?: 0)
        if (returnPage.totalRow <= 0) return returnPage

        val sql = NamedSQLUtil.getSelectSQL(Picture::class, paramMap)

        returnPage.data = this.namedParameterJdbcTemplate
                .query("$sql ORDER BY ID DESC LIMIT ${returnPage.startRow},${returnPage.pageSize}", paramMap, rowMapper)
        return returnPage
    }
}