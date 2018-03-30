package com.qiqinote.importdb

import com.qiqinote.po.Note
import com.qiqinote.po.User
import com.qiqinote.util.PasswordUtil
import com.qiqinote.util.sql.NamedSQLUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by vanki on 2018/1/19 11:34.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class DB {
    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    @Test
    fun note() {
        val result = mutableListOf<Note>()
        noteList(null, result)

        result.forEach({
            val paramMap = mutableMapOf<String, Any?>()
            it.id?.let{paramMap["id"] = it}
            it.parentId?.let{paramMap["parent_id"] = it}
            it.path?.let{paramMap["path"] = it}
            it.userId?.let{paramMap["user_id"] = it}
            it.type?.let{paramMap["type"] = it}
            it.noteNum?.let{paramMap["note_num"] = it}
            it.noteContentNum?.let{paramMap["note_content_num"] = it}
            it.secret?.let{paramMap["secret"] = it}
            it.password?.let{paramMap["password"] = it}
            it.title?.let{paramMap["title"] = it}
            it.keyword?.let{paramMap["keyword"] = it}
            it.sequence?.let{paramMap["sequence"] = it}
            it.description?.let{paramMap["description"] = it}
            it.viewNum?.let{paramMap["view_num"] = it}
            it.digest?.let{paramMap["digest"] = it}
            it.author?.let{paramMap["author"] = it}
            it.originUrl?.let{paramMap["origin_url"] = it}
            it.status?.let{paramMap["status"] = it}
            it.statusDescription?.let{paramMap["status_description"] = it}
            it.updateDatetime?.let{paramMap["update_datetime"] = it}
            it.createDatetime?.let{paramMap["create_datetime"] = it}
            it.isDel?.let{paramMap["is_del"] = it}

            this.namedParameterJdbcTemplate.update(NamedSQLUtil.getInsertSQL(Note::class, paramMap), paramMap)
        })
    }

    fun noteList(note: Note?, result: MutableList<Note>) {
        val parentId = note?.id ?: -1
        val list = this.namedParameterJdbcTemplate.query("select * from t_note where parent_id=" + parentId, mapOf<String, Any>(), BeanPropertyRowMapper(Note::class.java))
        if (list.isEmpty()) return


        list.forEach({
            if (it.parentId == -1L) {
                it.path = "-1"
            } else {
                if (note != null)
                    it.path = note.path + "_" + note.id
            }
            it.isDel = 0
            result.add(it)
            noteList(it, result)
        })
    }

    @Test
    fun user() {
        val list = this.namedParameterJdbcTemplate.query("select * from t_user", mapOf<String, Any>(), BeanPropertyRowMapper(User::class.java))

        list.forEach({
            if (it.id!! == 1L) {
                it.password = PasswordUtil.getEncPwd("vanki ")
            } else {
                it.password = PasswordUtil.getEncPwd("123456")
            }
            it.name = "user" + it.id
            it.isDel = 0


            val paramMap = mutableMapOf<String, Any?>()
            it.id?.let{paramMap["id"] = it}
            it.avatarId?.let{paramMap["avatar_id"] = it}
            it.name?.let{paramMap["name"] = it}
            it.alias?.let{paramMap["alias"] = it}
            it.password?.let{paramMap["password"] = it}
            it.gender?.let{paramMap["gender"] = it}
            it.status?.let{paramMap["status"] = it}
            it.motto?.let{paramMap["motto"] = it}
            it.phone?.let{paramMap["phone"] = it}
            it.email?.let{paramMap["email"] = it}
            it.qq?.let{paramMap["qq"] = it}
            it.weixin?.let{paramMap["weixin"] = it}
            it.weibo?.let{paramMap["weibo"] = it}
            it.registerOrigin?.let{paramMap["register_origin"] = it}
            it.registerIp?.let{paramMap["register_ip"] = it}
            it.description?.let{paramMap["description"] = it}
            it.createDatetime?.let{paramMap["create_datetime"] = it}
            it.isDel?.let{paramMap["is_del"] = it}

            this.namedParameterJdbcTemplate.update(NamedSQLUtil.getInsertSQL(User::class, paramMap), paramMap)
        })

    }
}