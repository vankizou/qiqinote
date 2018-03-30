package com.qiqinote.util

import org.im4java.core.ConvertCmd
import org.im4java.core.IMOperation

/**
 * Created by vanki on 2018/2/24 15:40.
 */
object ImageMagickUtil {
    /**
     * 根据坐标裁剪图片

     * @param srcPath  要裁剪图片的路径
     * *
     * @param destPath 裁剪图片后的路径
     * *
     * @param x_start  起始横坐标
     * *
     * @param y_start  起始纵坐标
     * *
     * @param x_end    结束横坐标
     * *
     * @param y_end    结束纵坐标
     */
    fun cutImage(srcPath: String, destPath: String, x_start: Int, y_start: Int, x_end: Int, y_end: Int) {
        val width = x_end - x_start
        val height = y_end - y_start
        val op = IMOperation()
        op.addImage(srcPath)
        /**
         * width： 裁剪的宽度 height： 裁剪的高度 x： 裁剪的横坐标 y： 裁剪的挫坐标
         */
        op.crop(width, height, x_start, y_start)
        op.addImage(destPath)
        val convert = ConvertCmd()
        convert.run(op)
    }

    /**
     * 根据尺寸等比缩放图片

     * @param width    缩放后的图片宽度
     * *
     * @param height   缩放后的图片高度
     * *
     * @param srcPath  源图片路径
     * *
     * @param destPath 缩放后图片的路径
     */
    fun resize(width: Int, height: Int, srcPath: String, destPath: String) {
        val op = IMOperation()
        op.addImage(srcPath)
        op.resize(width, height)
        op.addImage(destPath)
        val convert = ConvertCmd()
        convert.run(op)
    }

    @JvmStatic
    fun main(args: Array<String>) {
//        resizeByHeight(253, "/Users/vanki/Documents/company/lofficiel/data/1/111.jpg", "/Users/vanki/Documents/company/lofficiel/data/1/111_1.jpg");
    }

    /**
     * 根据宽度缩放图片

     * @param width    缩放后的图片宽度
     * *
     * @param srcPath  源图片路径
     * *
     * @param destPath 缩放后图片的路径
     */
    fun resizeByWidth(width: Int, srcPath: String, destPath: String) {
        val op = IMOperation()
        op.addImage(srcPath)
        op.resize(width, null)
        op.addImage(destPath)
        val convert = ConvertCmd()
        convert.run(op)
    }

    /**
     * 根据高度缩放图片

     * @param height   缩放后的图片高度
     * *
     * @param srcPath  源图片路径
     * *
     * @param destPath 缩放后图片的路径
     */
    fun resizeByHeight(height: Int, srcPath: String, destPath: String) {
        val op = IMOperation()
        op.addImage(srcPath)
        op.resize(null, height)
        op.addImage(destPath)
        val convert = ConvertCmd()
        convert.run(op)
    }
}