package com.qiqinote.anno

/**
 * @author vanki
 * @date 2019-12-08 00:07
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedLogin