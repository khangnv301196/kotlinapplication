package com.testing.kotlinapplication.network.model

data class DeliveryAddress(
    val address: String,
    val city: String,
    val created_at: String,
    val district: String,
    val id: Int,
    val name: String,
    val order_id: Int,
    val phone: String,
    val updated_at: String
)