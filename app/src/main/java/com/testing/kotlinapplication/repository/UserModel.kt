package com.testing.kotlinapplication.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserModel(
    @ColumnInfo(name = "CapVIP")
    var CapVIP: String,
    @ColumnInfo(name = "DiaChi_Quan")
    var DiaChi_Quan: String,
    @ColumnInfo(name = "DiaChi_SoNha")
    var DiaChi_SoNha: String,
    @ColumnInfo(name = "DiaChi_ThanhPho")
    var DiaChi_ThanhPho: String,
    @ColumnInfo(name = "GioiTinh")
    var GioiTinh: String,
    @ColumnInfo(name = "HinhAnh")
    var HinhAnh: String,
    @ColumnInfo(name = "HoTen")
    var HoTen: String,
    @ColumnInfo(name = "LoaiTaiKhoan")
    var LoaiTaiKhoan: String,
    @ColumnInfo(name = "NgaySinh")
    var NgaySinh: String,
    @ColumnInfo(name = "SDT")
    var SDT: String,
    @ColumnInfo(name = "TenDangNhap")
    var TenDangNhap: String,
    @ColumnInfo(name = "TrangThai")
    var TrangThai: Boolean,
    @ColumnInfo(name = "access_token")
    var access_token: String,
    @ColumnInfo(name = "created_at")
    var created_at: String,
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "email_verified_at")
    var email_verified_at: String,
    @ColumnInfo(name = "expires_at")
    val expires_at: String,
//    @ColumnInfo(name = "id")
//    val id: Int,
    @ColumnInfo(name = "token_type")
    val token_type: String,
    @ColumnInfo(name = "updated_at")
    val updated_at: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null
}