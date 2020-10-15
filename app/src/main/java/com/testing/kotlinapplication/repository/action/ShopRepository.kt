package com.testing.kotlinapplication.repository.action

import android.content.Context
import android.util.Base64
import androidx.lifecycle.LiveData
import com.testing.kotlinapplication.network.model.User
import com.testing.kotlinapplication.repository.UserModel
import com.testing.kotlinapplication.roomdata.ShopDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ShopRepository {
    companion object {
        var shopDatabase: ShopDatabase? = null
        var userModel: LiveData<UserModel>? = null
        fun initializeDB(context: Context): ShopDatabase {
            return ShopDatabase.getDatabaseClient(context)
        }

        fun insertData(
            context: Context, user: User
        ) {
            shopDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                val userModel = UserModel(
                    user.CapVIP ?: "",
                    user.DiaChi_Quan ?: "",
                    user.DiaChi_SoNha ?: "",
                    user.DiaChi_ThanhPho ?: "",
                    user.GioiTinh ?: "",
                    user.HinhAnh ?: "",
                    user.HoTen ?: "",
                    user.LoaiTaiKhoan ?: 3,
                    user.NgaySinh ?: "",
                    user.SDT ?: "",
                    user.TenDangNhap ?: "",
                    user.TrangThai ?: true,
                    user.access_token ?: "",
                    user.created_at ?: "",
                    user.email ?: "",
                    user.email_verified_at ?: "",
                    user.expires_at ?: "",
                    user.token_type ?: "",
                    user.updated_at ?: "",
                    user.id ?: 0
                )
                shopDatabase!!.ShopDAO().insertData(userModel)
            }
        }

        fun checkUser(context: Context, userName: String): LiveData<UserModel>? {
            shopDatabase = initializeDB(context)
            userModel = shopDatabase!!.ShopDAO().findByName(userName)
            return userModel
        }

        fun getUser(context: Context, id: Int): LiveData<UserModel>? {
            shopDatabase = initializeDB(context)
            userModel = shopDatabase!!.ShopDAO().findByID(id)
            return userModel
        }
    }
}