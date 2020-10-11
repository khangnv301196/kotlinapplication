package com.testing.kotlinapplication.network.model

data class LoginRespone(
    val message: String,
    val status: Int,
    val user: User
)