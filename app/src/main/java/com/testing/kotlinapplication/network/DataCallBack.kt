package com.testing.kotlinapplication.network

interface DataCallBack<T> {
    fun Complete(respon: T)
    fun Error(error: Throwable)
}