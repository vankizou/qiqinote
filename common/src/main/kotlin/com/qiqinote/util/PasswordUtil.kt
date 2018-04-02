package com.qiqinote.util

import java.util.*

/**
 * Created by vanki on 2018/1/17 22:24.
 */
object PasswordUtil {
    private val pwdKey = "Vanki!@#1QI"
    private val noteKey = "!@#Note123"

    fun getEncNoteId(noteId: Long) = String(Base64.getEncoder().encode(getRc4Str(noteId.toString(), noteKey).toByteArray(Charsets.UTF_8))).trim()

    fun getDecNoteId(encNoteId: String) = getRc4Str(String(Base64.getDecoder().decode(encNoteId)), noteKey).toLong()

    fun getEncPwd(pwd: String) = String(Base64.getEncoder().encode(getRc4Str(pwd, pwdKey).toByteArray(Charsets.UTF_8))).trim()

    fun getDecPwd(encPwd: String) = getRc4Str(String(Base64.getDecoder().decode(encPwd)), pwdKey)

    private fun getRc4Str(aInput: String, aKey: String): String {
        val iS = IntArray(256)
        val iK = ByteArray(256)

        for (i in 0..255) {
            iS[i] = i
        }
        for (i in 0..255) {
            iK[i] = aKey[i % aKey.length].toByte()
        }
        var j = 0
        for (i in 0..254) {
            j = (j + iS[i] + iK[i].toInt()) % 256
            val temp = iS[i]
            iS[i] = iS[j]
            iS[j] = temp
        }
        var i = 0
        j = 0
        val iInputChar = aInput.toCharArray()
        val iOutputChar = CharArray(iInputChar.size)
        for (x in iInputChar.indices) {
            i = (i + 1) % 256
            j = (j + iS[i]) % 256
            val temp = iS[i]
            iS[i] = iS[j]
            iS[j] = temp
            val t = (iS[i] + iS[j] % 256) % 256
            val iY = iS[t]
            val iCY = iY
            iOutputChar[x] = (iInputChar[x].toInt() xor iCY).toChar()
        }
        return String(iOutputChar)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(PasswordUtil.getEncNoteId(1))
        println(PasswordUtil.getEncNoteId(15))
        println(PasswordUtil.getEncNoteId(175))
        println(PasswordUtil.getEncNoteId(1755))
        println(PasswordUtil.getEncNoteId(17552))
        println(PasswordUtil.getDecNoteId(PasswordUtil.getEncNoteId(2812345678)))

        println(PasswordUtil.getEncPwd("123456"))
        println(PasswordUtil.getDecPwd(PasswordUtil.getEncPwd("12312345678901234567890123456789012345678")))
    }
}
