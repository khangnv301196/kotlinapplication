package com.testing.kotlinapplication.network.model

data class DanhSachGioHang(
    val AnhChinh: String,
    val DongGia: String,
    val KichThuoc: String,
    val MaChiTietSanPham: Int,
    val MaKhachHang: Int,
    val Mau: String,
    val MoTa: Any,
    val SoLuong: Int,
    val TenSP: String,
    val created_at: String,
    val id: Int,
    val updated_at: String
)