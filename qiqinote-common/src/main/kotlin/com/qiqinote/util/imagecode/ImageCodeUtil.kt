package com.qiqinote.util.imagecode

import sun.misc.BASE64Encoder
import java.awt.Color
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by vanki on 2018/2/26 14:09.
 */
open class ImageCodeUtil {
    protected val random = Random()
    // 字体样式, 加粗, 斜体...
    private val fontStyleArr = intArrayOf(Font.BOLD, Font.ITALIC + Font.BOLD, Font.PLAIN + Font.ITALIC + Font.BOLD)
    private val defaultStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
    private val format = "PNG"
    // 背景色范围
    private val rgbBackground = intArrayOf(200, 250)
    private val rgbDistrub = intArrayOf(1, 255)
    private val rgbString = intArrayOf(1, 100)
    // 字体
    private var fontNameArr: Array<String> = arrayOf("Default")
    // 验证码
    private var code = ""
    // 图片的宽度。
    private var width = 140
    // 图片的高度。
    private var height = 40
    // 验证码字符个数
    private var codeCount = 4
    // 验证码干扰线数
    private var lineCount = 20
    // 验证码图片Buffer
    private lateinit var buffImg: BufferedImage

    constructor () {
        createImage()
    }

    constructor(code: String) {
        this.code = code
        this.codeCount = code.length
        createImage()
    }

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
        createImage()
    }

    constructor(width: Int, height: Int, code: String) {
        this.width = width
        this.height = height
        this.code = code
        this.codeCount = code.length
        createImage()
    }

    constructor(width: Int, height: Int, code: String, lineCode: Int) {
        this.width = width
        this.height = height
        this.code = code
        this.lineCount = lineCode
        createImage()
    }

    constructor(width: Int, height: Int, codeCount: Int) {
        this.width = width
        this.height = height
        this.codeCount = codeCount
        createImage()
    }

    constructor(width: Int, height: Int, codeCount: Int, lineCount: Int) {
        this.width = width
        this.height = height
        this.codeCount = codeCount
        this.lineCount = lineCount
        createImage()
    }

    protected fun getScopeNum(min: Int, max: Int): Int {
        return random.nextInt(max) % (max - min + 1) + min
    }

    protected fun getRandomChar(str: String): Char {
        val index = random.nextInt(str.length)
        return str[index]
    }

    // 得到随机字符
    protected open fun drawString(): String {
        this.code = ""
        val len = defaultStr.length - 1
        var r: Double
        for (i in 0..this.codeCount - 1) {
            r = Math.random() * len
            this.code = this.code + defaultStr[r.toInt()]
        }
        return this.code
    }

    @Throws(IOException::class)
    fun write(outImgPath: String) {
        ImageIO.write(buffImg, format, File(outImgPath))
    }

    @Throws(IOException::class)
    fun write(os: OutputStream?) {
        try {
            ImageIO.write(buffImg, format, os!!)
        } catch (e: IOException) {
            throw e
        } finally {
            os?.close()
        }
    }

    /**
     * 图片字节转base64字符, html解码: <img src="data:image/png;base64,这里放BASE64字符"></img>

     * @return
     * *
     * *
     * @throws IOException
     */
    fun writeBASE64(): String {
        var os: ByteArrayOutputStream? = null
        try {
            os = ByteArrayOutputStream(2048)
            ImageIO.write(buffImg, format, os)
            return BASE64Encoder().encode(os.toByteArray())
        } catch (e: IOException) {
            throw e
        } finally {
            if (os != null) os.close()
        }
    }

    fun getBuffImg(): BufferedImage {
        return buffImg
    }

    fun getCode(): String {
        return this.code
    }

    // 生成图片
    private fun createImage() {
        if (this.code.length == 0) {
            this.code = drawString()// 得到随机字符
            this.codeCount = this.code.length
        }

        val fontWidth = width / codeCount// 字体的宽度
        val fontHeight = height - 2// 字体的高度
        val codeY = height - 6

        // 图像buffer
        buffImg = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = buffImg.graphics
        //Graphics2D g = buffImg.createGraphics();
        // 设置背景色
        g.color = getRandColor(rgbBackground[0], rgbBackground[1])
        g.fillRect(0, 0, width, height)

        // 设置字体
        val font = getFont(fontHeight)
        g.font = font

        // 设置干扰线
        for (i in 0..lineCount - 1) {
            val xs = random.nextInt(width)
            val ys = random.nextInt(height)
            val xe = xs + random.nextInt(width)
            val ye = ys + random.nextInt(height)
            g.color = getRandColor(rgbDistrub[0], rgbDistrub[1])
            g.drawLine(xs, ys, xe, ye)
        }

        // 添加噪点
        val yawpRate = 0.01f// 噪声率
        val area = (yawpRate * width.toFloat() * height.toFloat()).toInt()
        for (i in 0..area - 1) {
            val x = random.nextInt(width)
            val y = random.nextInt(height)

            buffImg.setRGB(x, y, random.nextInt(255))
        }

        for (i in 0..this.codeCount - 1) {
            val strRand = this.code.substring(i, i + 1)
            g.color = getRandColor(rgbString[0], rgbString[1])
            // g.drawString(a,x,y);
            // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
            g.drawString(strRand, i * fontWidth + 1, codeY)
        }
    }

    // 得到随机颜色
    private fun getRandColor(fc: Int, bc: Int): Color {// 给定范围获得随机颜色
        var fcTmp = fc
        var bcTmp = bc
        if (fcTmp > 255) fcTmp = 255
        if (bcTmp > 255) bcTmp = 255
        val r = getScopeNum(fcTmp, bcTmp)
        val g = getScopeNum(fcTmp, bcTmp)
        val b = getScopeNum(fcTmp, bcTmp)
        return Color(r, g, b)
    }

    /**
     * 产生随机字体
     */
    private fun getFont(fontSize: Int): Font {
        initFontNameArr()
        val indexName = random.nextInt(fontNameArr.size)
        val indexStyle = random.nextInt(fontStyleArr.size)
        return Font(fontNameArr[indexName], fontStyleArr[indexStyle], fontSize)
    }

    private fun initFontNameArr() {
        fontNameArr = GraphicsEnvironment.getLocalGraphicsEnvironment().availableFontFamilyNames
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            ImageCodeUtil("12+343").write("/data/aa.jpeg")
            ImageCodeUtil(" ").write("/data/aa.jpeg")
        }
    }
}