package com.testing.kotlinapplication.network.model

data class OrderResponseItem(
    val MaKhachHang: Int,
    var TrangThai: Int,
    val chi_tiet_hoa_don: List<ChiTietHoaDon>,
    val created_at: String,
    val delivery_address: DeliveryAddress,
    val id: Int,
    val khach_hang: KhachHang,
    val updated_at: String,
    val ngaytao: String
)