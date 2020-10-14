package com.testing.kotlinapplication.network.model

data class Data(
    val AnhChinh: String,
    val DongGia: String,
    val MaLoaiSanPham: Int,
    val TenLoaiSanPham: String,
    val TenSP: String,
    val created_at: String,
    val id: Int,
    val rating: Int,
    val updated_at: String,
    var viewType: Int = 0
) {
    constructor() : this("", "0", 0, "", "", "", 0, 3,"")
}