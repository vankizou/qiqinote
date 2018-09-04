package com.qiqinote.util

import java.security.MessageDigest

/**
 * Created by vanki on 2018/2/26 15:26.
 */
object MD5Util {
    private val HEX = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    private val saltValue = "B602CDB2BB5EEEB3187BCCE5CAAE9D11"

    fun getMD5(str: String) = encrypt(str, "md5")

    private fun encrypt(message: String, type: String): String {
        if (StringUtil.isEmpty(message)) return message
        val digest = MessageDigest.getInstance(type)
        val newMessagg = getMessageAndSalt(message)
        digest.update(newMessagg.toByteArray(charset("UTF-8")))
        val b = digest.digest()
        return String(hex(b))
    }

    fun getMessageAndSalt(message: String?): String {
        if (message != null && StringUtil.isNotEmpty(message)) {
            return "$message{$saltValue}"
        } else {
            return "内容不能为空"
        }
    }

    private fun hex(bytes: ByteArray): CharArray {
        val nBytes = bytes.size
        val result = CharArray(2 * nBytes)
        var j = 0
        for (i in 0..nBytes - 1) {
            result[j++] = HEX[(0xF0 and bytes[i].toInt()).ushr(4)]
            result[j++] = HEX[0x0F and bytes[i].toInt()]
        }
        return result
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(MD5Util.getMD5("234"))
    }
}