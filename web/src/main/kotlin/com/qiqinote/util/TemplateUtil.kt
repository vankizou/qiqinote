package com.qiqinote.util

import java.util.*

/**
 * Created by vanki on 2018/3/30 10:37.
 */
object TemplateUtil {
    val key_exportNoteTemp_title = "###NOTE_TITLE###"
    val key_exportNoteTemp_content = "###NOTE_CONTENT###"
    private val exportNoteTempList = ArrayList<String>()

    init {
        val tempUrl = TemplateUtil::class.java.getClassLoader().getResource("temp");
        if (tempUrl != null) {
            initExportNoteTemp(tempUrl.getPath());
        }
    }

    fun getExportNoteTempList(): List<String>? {
        return if (exportNoteTempList.isEmpty()) null else exportNoteTempList
    }

    private fun initExportNoteTemp(tempRootPath: String) {
        if (StringUtil.isEmpty(tempRootPath)) return
        var data = FileUtil.readFile("$tempRootPath/exportNoteTemp.html")
        if (StringUtil.isEmpty(data)) return
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