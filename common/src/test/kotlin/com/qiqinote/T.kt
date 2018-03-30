package com.qiqinote

/**
 * Created by vanki on 2018/1/22 16:26.
 */
class T: A {
    override fun a(a: Int) {
        println(a)
    }

    fun b(a: Int = 1000) {
        println(a)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            T().b()
        }
    }
}


interface A {
    fun a(a: Int = 100)
}