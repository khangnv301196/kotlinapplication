package com.testing.kotlinapplication.network.model

data class DetailProductReponse(
    val AnhChinh: String,
    val DongGia: String,
    val MaLoaiSanPham: Int,
    val MoTa: String,
    val TenSP: String,
    val chi_tiet_san_pham: List<Any>,
    val created_at: String,
    val id: Int,
    val rating: Int,
    val san_pham_bo_suu_tap: List<Any>,
    val updated_at: String
)