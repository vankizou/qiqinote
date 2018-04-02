package com.qiqinote.util

import org.apache.log4j.Logger
import java.io.*
import java.nio.channels.FileChannel
import java.util.*

/**
 * Created by vanki on 2018/2/24 16:55.
 */
object FileUtil {
    private val log = Logger.getLogger(FileUtil::class.java)

    fun appendFile(filePath: String, content: String) {
        writeFile(filePath, content, true)
    }

    fun writeFile(filePath: String, content: String) {
        writeFile(filePath, content, false)
    }

    private fun writeFile(filePath: String, content: String?, isAppend: Boolean) {
        if (content == null) return
        val file = File(filePath)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()

        var fw: FileWriter? = null
        try {
            if (isAppend) {
                fw = FileWriter(file, true)
            } else {
                fw = FileWriter(file)
            }
            fw.write(content)
            fw.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (fw != null) fw.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun readFileOfLine(filePath: String): List<String>? {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) return null

        var fis: FileInputStream? = null
        var isr: InputStreamReader? = null
        var br: BufferedReader? = null
        val list = ArrayList<String>()

        try {
            fis = FileInputStream(file)
            isr = InputStreamReader(fis)
            br = BufferedReader(isr)
            var line: String?
            while (true) {
                line = br.readLine()
                if (line == null) break
                list.add(line)
            }
        } catch (e: Exception) {
            log.error("", e)
        } finally {
            try {
                if (fis != null) fis.close()
                if (isr != null) isr.close()
                if (br != null) br.close()
            } catch (e: IOException) {
                log.error("", e)
            }

        }
        return list
    }

    fun readFile(file: File): String? {
        if (!file.exists() || !file.isFile) return null

        var fis: FileInputStream = FileInputStream(file)
        val str = readFile(fis)
        fis.close()
        return str
    }

    fun readFile(inputStream: InputStream): String? {
        var isr: InputStreamReader? = null
        var br: BufferedReader? = null
        val sb = StringBuffer()

        try {
            isr = InputStreamReader(inputStream)
            br = BufferedReader(isr)
            var line: String?
            while (true) {
                line = br.readLine()
                if (line == null) break
                sb.append(line)
                sb.append("\r\n")
            }
        } catch (e: Exception) {
            log.error("", e)
        } finally {
            try {
                if (isr != null) isr.close()
                if (br != null) br.close()
            } catch (e: IOException) {
                log.error("", e)
            }

        }
        return sb.toString()
    }


    /**
     * 复制文件
     *
     * @param srcFile
     * @param destFile
     *
     * @return
     */
    fun copyFile(srcFile: File?, destFile: File?): Boolean {
        if (srcFile == null || destFile == null || !srcFile.exists()) return false
        val parentFile = destFile.parentFile
        if (!parentFile.exists()) parentFile.mkdirs()

        var fi: FileInputStream? = null
        var fo: FileOutputStream? = null
        var cIn: FileChannel? = null
        var cOut: FileChannel? = null
        try {
            fi = FileInputStream(srcFile)
            fo = FileOutputStream(destFile)
            cIn = fi.channel
            cOut = fo.channel
            cIn!!.transferTo(0, cIn.size(), cOut)
            return true
        } catch (e: Exception) {
            log.error("文件复制失败! 异常信息", e)
        } finally {
            try {
                if (cOut != null) cOut.close()
                if (cIn != null) cIn.close()
                if (fo != null) fo.close()
                if (fi != null) fi.close()
            } catch (e: Exception) {
                log.error("文件复制失败! 异常信息", e)
            }

        }
        return false
    }

    /**
     * 设置文件权限

     * @param file
     * *
     * @param permissionNum
     * *
     * *
     * @throws IOException
     */
    fun setFilePermission(file: File?, permissionNum: Int) {
        if (file != null && file.exists()) {
            val sb = StringBuffer().append("chmod ").append(permissionNum).append(" ").append(file.absolutePath)
            Runtime.getRuntime().exec(sb.toString())
        }
    }

    fun setFilePermission(path: String, permissionNum: Int) {
        val sb = StringBuffer().append("chmod ").append(permissionNum).append(" ").append(path)
        Runtime.getRuntime().exec(sb.toString())
    }

    fun setFolderAndAllSubPermission(path: String, permissionNum: Int) {
        val sb = StringBuffer().append("chmod -R ").append(permissionNum).append(" ").append(path)
        Runtime.getRuntime().exec(sb.toString())
    }
}