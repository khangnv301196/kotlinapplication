package com.testing.kotlinapplication.network.model

data class TopResponse(
    val danhSachBoSuuTap: List<DanhSachBoSuuTap>,
    val danhSachSanPham: List<Data>,
    val sliders: List<Slider>,
    val status: Int
)