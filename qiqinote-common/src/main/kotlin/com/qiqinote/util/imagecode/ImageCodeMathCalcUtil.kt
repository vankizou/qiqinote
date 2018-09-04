package com.qiqinote.util.imagecode

/**
 * Created by vanki on 2018/2/26 14:29.
 */
class ImageCodeMathCalcUtil : ImageCodeUtil {
    private val mathCalcSign    // 加减乘除
        get() = "+-*/"
    private lateinit var result: String

    constructor() : super(180, 30)

    constructor(width: Int, height: Int) : super(width, height)

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val imageCode = ImageCodeMathCalcUtil()
//            println(imageCode.writeBASE64());
            imageCode.write("/data/aa.png")
            println(imageCode.getCode())
            println(imageCode.getResult())
        }
    }

    fun getResult(): String {
        return this.result
    }

    /**
     * 生成随机数学四则运算的字符串 <br></br>
     * 数组0: 运算公式, 如: 43减1=? <br></br>
     * 数组1: 运算值, 如: 42

     * @return
     */
    override fun drawString(): String {
        val calcSignChar = getRandomChar(mathCalcSign)

        var arr = when (calcSignChar) {
            '+' -> mathCalcPlus('+')
            '-' -> mathCalcSub('-')
            '*' -> mathCalcMul("*")
            '/' -> mathCalcDivide("÷")
            '加' -> mathCalcPlus('加')
            '减' -> mathCalcSub('减')
            '乘' -> mathCalcMul("乘以")
            '除' -> mathCalcDivide("除以")
            else -> mathCalcPlus('+')
        }
        arr[0] = arr[0] + "=?"
        this.result = arr[1]
        return arr[0]
    }


    /**
     * 加法计算, 两个加数中最多只有一个为两位数, 100以内

     * @return
     */
    private fun mathCalcPlus(signChar: Char): Array<String> {
        val val1 = getScopeNum(0, 90)
        val val2: Int
        if (val1 > 9) {
            val2 = getScopeNum(0, 9)
        } else {
            val2 = getScopeNum(0, 90)
        }
        val code = StringBuffer().append(val1).append(signChar).append(val2).toString()
        val codeVal = (val1 + val2).toString()
        return arrayOf(code, codeVal)
    }

    /**
     * 减法计算, 值为正整数, 100以内

     * @param signChar
     * *
     * *
     * @return
     */
    private fun mathCalcSub(signChar: Char): Array<String> {
        val val1 = getScopeNum(0, 99)
        val val2 = getScopeNum(0, if (val1 > 9) 9 else val1)

        val code = StringBuffer().append(val1).append(signChar).append(val2).toString()
        val codeVal = (val1 - val2).toString()

        return arrayOf(code, codeVal)
    }

    /**
     * 乘法计算, 乘数与被乘数都是1位

     * @param signChar
     * *
     * *
     * @return
     */
    private fun mathCalcMul(signChar: String): Array<String> {
        val val1 = getScopeNum(0, 9)
        val val2 = getScopeNum(0, 9)

        val code = StringBuffer().append(val1).append(signChar).append(val2).toString()
        val codeVal = (val1 * val2).toString()

        return arrayOf(code, codeVal)
    }

    /**
     * 除法计算, 值为整数, 值和除数皆为1位

     * @param signChar
     * *
     * *
     * @return
     */
    private fun mathCalcDivide(signChar: String): Array<String> {
        val val1 = getScopeNum(1, 9)
        val val2 = getScopeNum(1, 9)

        val code = StringBuffer().append(val1 * val2).append(signChar).append(val1).toString()
        val codeVal = val2.toString()

        return arrayOf(code, codeVal)
    }
}