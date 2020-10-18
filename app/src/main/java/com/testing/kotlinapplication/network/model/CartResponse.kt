package com.testing.kotlinapplication.network.model

data class CartResponse(
    val danhSachGioHang: List<DanhSachGioHang>,
    val status: Int
)