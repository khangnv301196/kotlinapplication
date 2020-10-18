package com.testing.kotlinapplication.network.model

data class DanhSachGioHang(
    val MaChiTietSanPham: Int,
    val MaKhachHang: Int,
    val SoLuong: Int,
    val chi_tiet_san_pham: ChiTietSanPham,
    val created_at: String,
    val id: Int,
    val updated_at: String
)