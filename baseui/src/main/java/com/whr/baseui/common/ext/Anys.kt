package com.whr.baseui.common.ext

import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
fun <T> Any.getGenericClass(index: Int): Class<T> {
    return (javaClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.get(index) as? Class<T>
        ?: throw IllegalStateException()
}

fun tryCatch(block: () -> Unit) {
    return try {
        block.invoke()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}
