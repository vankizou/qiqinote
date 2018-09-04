package com.qiqinote.util

import java.io.InputStream


/**
 * Created by vanki on 2018/3/29 23:32.
 */
object ImageTypeUtil {
    fun getFileType(`is`: InputStream): String? {
        val c1 = `is`.read()
        val c2 = `is`.read()
        var c3 = `is`.read()

        var mimeType: String? = null

        if (c1 == 'G'.toInt() && c2 == 'I'.toInt() && c3 == 'F'.toInt()) { // GIF
            `is`.skip(3)
            mimeType = "gif"
        } else if (c1 == 0xFF && c2 == 0xD8) { // JPG
            while (c3 == 255) {
                val marker = `is`.read()
                val len = readInt(`is`, 2, true)
                if (marker == 192 || marker == 193 || marker == 194) {
                    `is`.skip(1)
                    mimeType = "jpeg"
                    break
                }
                `is`.skip(len.toLong() - 2)
                c3 = `is`.read()
            }
        } else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
            `is`.skip(15)
            `is`.skip(2)
            mimeType = "png"
        } else if (c1 == 66 && c2 == 77) { // BMP
            `is`.skip(15)
            `is`.skip(2)
            mimeType = "bmp"
        }
        return mimeType
    }

    private fun readInt(`is`: InputStream, noOfBytes: Int, bigEndian: Boolean): Int {
        var ret = 0
        var sv = if (bigEndian) (noOfBytes - 1) * 8 else 0
        val cnt = if (bigEndian) -8 else 8
        for (i in 0 until noOfBytes) {
            ret = ret or (`is`.read() shl sv)
            sv += cnt
        }
        return ret
    }
}