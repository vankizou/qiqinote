package com.qiqinote.util

import org.junit.Test

/**
 * Created by vanki on 2018/3/5 17:24.
 */
class EntityUtilTest {
    class A {
        var v1: String? = null
        var v2: String? = null

        override fun toString(): String {
            return "A(v1=$v1, v2=$v2)"
        }
    }

    class B {
        var v1: String? = null
        var v2: Int? = null
        var v3: String? = null

        override fun toString(): String {
            return "B(v1=$v1, v2=$v2, v3=$v3)"
        }
    }

    @Test
    fun testCopyOfDiffObj() {
        println("===== 同类型同名称的属性值 ====")

        var a1 = A()
        a1.v1 = "a1.v1"

        var b2 = B()
        b2.v1 = "b2.v1"
        b2.v2 = 123
        b2.v3 = "b2.v3"

        EntityUtil.copyValOfDiffObj(a1, b2)
        println(a1)
        println(b2)

        println()
        println("===== 同类型同名称且值为null ====")

        a1 = A()
        a1.v1 = "xxx"

        b2 = B()
        b2.v1 = "b2.v1"
        b2.v2 = 123
        b2.v3 = "b2.v3"

        EntityUtil.copyValIfNullOfDiffObj(a1, b2)
        println(a1)
        println(b2)

        println()
        println("===== 同类型同名称且值为空 ====")

        a1 = A()
        a1.v1 = ""

        b2 = B()
        b2.v1 = "b2.v1"
        b2.v2 = 123
        b2.v3 = "b2.v3"

        EntityUtil.copyValIfEmptyOfDiffObj(a1, b2)
        println(a1)
        println(b2)

        println("===== 同类型同名称 ====")
        EntityUtil.copyValIfEmpty(A::class, b2)
        println(a1)
        println(b2)
    }

    @Test
    fun testCopy() {
        println("===== copy ====")

        var a1 = A()
        a1.v1 = "a1.v1"

        var a2 = A()
        a2.v1 = "a2.v1"
        a2.v2 = "a2.v2"

        EntityUtil.copyVal(a1, a2)
        println(a1)
        println(a2)

        println()
        println("===== copyIfNull ====")

        a1 = A()
        a1.v1 = "a1.v1"

        a2 = A()
        a2.v1 = "a2.v1"
        a2.v2 = "a2.v2"

        EntityUtil.copyValIfNull(a1, a2)
        println(a1)
        println(a2)

        println()
        println("===== copyIfEmpty ====")

        a1 = A()
        a1.v1 = ""

        a2 = A()
        a2.v1 = "a2.v1"
        a2.v2 = "a2.v2"

        EntityUtil.copyValIfEmpty(a1, a2)
        println(a1)
        println(a2)

        println("===== 同类型同名称 ====")
        println(EntityUtil.copyValIfEmpty(A::class, a2))
        println(a2)
    }
}