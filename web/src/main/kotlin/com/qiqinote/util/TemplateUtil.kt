package com.qiqinote.util

import org.apache.commons.io.FileUtils
import org.apache.coyote.http11.Constants.a
import org.apache.log4j.Logger
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * Created by vanki on 2018/3/30 10:37.
 */
object TemplateUtil {
    private val log = Logger.getLogger(TemplateUtil::class.java)
    val key_exportNoteTemp_title = "###NOTE_TITLE###"
    val key_exportNoteTemp_content = "###NOTE_CONTENT###"
    private val exportNoteTempList = ArrayList<String>()

    init {
        initExportNoteTemp();
    }

    fun getExportNoteTempList(): List<String>? {
        return if (exportNoteTempList.isEmpty()) null else exportNoteTempList
    }

    private fun initExportNoteTemp() {

        val stream = TemplateUtil::class.java.getClassLoader().getResourceAsStream("temp/exportNoteTemp.html")
        var data = FileUtil.readFile(stream)
        stream.close()

        if (StringUtil.isEmpty(data)) {
            log.error("markdown模版数据为空!")
            return
        }
        /**
         * 从上到下，顺序分割要填充的数据
         */
        // 标题
        var arr = data!!.split(key_exportNoteTemp_title.toRegex(), 2).toTypedArray()
        if (arr.size == 2) {
            exportNoteTempList.add(arr[0])
            exportNoteTempList.add(key_exportNoteTemp_title)
            data = arr[1]
        }
        // 标题
        arr = data.split(key_exportNoteTemp_title.toRegex(), 2).toTypedArray()
        if (arr.size == 2) {
            exportNoteTempList.add(arr[0])
            exportNoteTempList.add(key_exportNoteTemp_title)
            data = arr[1]
        }
        // 内容
        arr = data.split(key_exportNoteTemp_content.toRegex(), 2).toTypedArray()
        if (arr.size == 2) {
            exportNoteTempList.add(arr[0])
            exportNoteTempList.add(key_exportNoteTemp_content)
            data = arr[1]
        }
        exportNoteTempList.add(data)
    }
}