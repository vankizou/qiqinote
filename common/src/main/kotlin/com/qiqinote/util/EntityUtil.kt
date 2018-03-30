package com.qiqinote.util

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Created by vanki on 2018/3/5 15:34.
 */
object EntityUtil {
    /**
     * 同类型两对象复制属性值操作, dest字段值 = src字段值
     */
    fun <T : Any> copyVal(dest: T, src: T, vararg excludeFields: String) = EntityUtil.copyValCommon(dest, src, CopyType.ALL, excludeFields)

    /**
     * 同类型两对象复制属性值操作, dest字段值 = src字段值  (仅当dest字段值为 <null> 时才拷贝)
     */
    fun <T : Any> copyValIfNull(dest: T, src: T) = EntityUtil.copyValCommon(dest, src, CopyType.JUST_DEST_NULL, null)

    /**
     * 同类型两对象复制属性值操作, dest字段值 = src字段值  (仅当dest字段值为 <null> 或 <空> 时才拷贝)
     */
    fun <T : Any> copyValIfEmpty(dest: T, src: T) = EntityUtil.copyValCommon(dest, src, CopyType.JUST_DEST_EMPTY, null)

    /**
     * 同类型两对象复制属性值操作
     */
    private fun <T : Any> copyValCommon(dest: T, src: T, copyType: CopyType, excludeFields: Array<out String>?): T {
        val kClass = dest::class
        if (!kClass.equals(src::class)) return dest

        val jClass = kClass.java
        val members = kClass.memberProperties

        var srcValTmp: Any?
        var destValTmp: Any?

        for (kProperty in members) {
            if ("PUBLIC" != kProperty.visibility.toString()) continue
            if (excludeFields != null && excludeFields.contains(kProperty.name)) continue

            destValTmp = kProperty.call(dest)
            srcValTmp = kProperty.call(src)
            if (destValTmp == null && srcValTmp == null) continue

            if (copyType == CopyType.ALL ||
                    (copyType == CopyType.JUST_DEST_NULL && destValTmp == null) ||
                    (copyType == CopyType.JUST_DEST_EMPTY && ((destValTmp == null) || (destValTmp is String && "".equals(destValTmp.trim()))))
            ) {
                val jField = jClass.getDeclaredField(kProperty.name)
                val bakAccessible = jField.isAccessible
                jField.isAccessible = true
                jField.set(dest, srcValTmp)
                jField.isAccessible = bakAccessible
            }
        }
        return dest
    }

    /**
     * 不同类型两对象复制属性值操作(只有字段名与字段类型一致才会copy), dest字段值 = src字段值
     */
    fun <T1 : Any, T2 : Any> copyValOfDiffObj(dest: T1, src: T2) = EntityUtil.copyValOfDiffObjCommon(dest, src, CopyType.ALL)

    /**
     * 同类型两对象复制属性值操作(只有字段名与字段类型一致才会copy), dest字段值 = src字段值  (仅当dest字段值为 <null> 时才拷贝)
     */
    fun <T1 : Any, T2 : Any> copyValIfNullOfDiffObj(dest: T1, src: T2) = EntityUtil.copyValOfDiffObjCommon(dest, src, CopyType.JUST_DEST_NULL)

    /**
     * 同类型两对象复制属性值操作(只有字段名与字段类型一致才会copy), dest字段值 = src字段值  (仅当dest字段值为 <null> 或 <空> 时才拷贝)
     */
    fun <T1 : Any, T2 : Any> copyValIfEmptyOfDiffObj(dest: T1, src: T2) = EntityUtil.copyValOfDiffObjCommon(dest, src, CopyType.JUST_DEST_EMPTY)

    private fun <T1 : Any, T2 : Any> copyValOfDiffObjCommon(dest: T1, src: T2, copyType: CopyType): T1 {
        val kDestClass = dest::class
        val kSrcClass = src::class
        val jDestClass = kDestClass.java
        val destMembers = kDestClass.memberProperties

        val mapOfSrcProperties: MutableMap<String, KProperty1<out T2, Any?>> = mutableMapOf()
        kSrcClass.memberProperties.forEach { mapOfSrcProperties.put(it.name, it) }

        var srcValTmp: Any?
        var destValTmp: Any?
        var kPropertyName: String?

        for (destKProperty in destMembers) {
            if ("PUBLIC" != destKProperty.visibility.toString()) continue
            kPropertyName = destKProperty.name

            val srcKProperty = mapOfSrcProperties.get(kPropertyName) ?: continue
            if (!srcKProperty.returnType.equals(destKProperty.returnType)) continue // 同步不同类型

            destValTmp = destKProperty.call(dest)
            srcValTmp = srcKProperty.call(src)
            if (destValTmp == null && srcValTmp == null) continue

            if (copyType == CopyType.ALL ||
                    (copyType == CopyType.JUST_DEST_NULL && destValTmp == null) ||
                    (copyType == CopyType.JUST_DEST_EMPTY && ((destValTmp == null) || (destValTmp is String && "".equals(destValTmp.trim()))))
            ) {
                /**
                 * 赋值
                 */
                val jDestField = jDestClass.getDeclaredField(kPropertyName)
                val bakAccessible = jDestField.isAccessible
                jDestField.isAccessible = true
                jDestField.set(dest, srcValTmp)
                jDestField.isAccessible = bakAccessible
            }
        }
        return dest
    }

    private enum class CopyType {
        /**
         * 拷贝所有字段值
         */
        ALL,

        /**
         * 当目标类字段值为null时, 拷贝来源字段值
         */
        JUST_DEST_NULL,

        /**
         * 当目标类字段值为null或""时, 拷贝来源字段值
         */
        JUST_DEST_EMPTY,
    }
}