package com.testing.kotlinapplication.repository.action

import android.content.Context
import android.util.Base64
import androidx.lifecycle.LiveData
import com.testing.kotlinapplication.network.model.User
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.UserModel
import com.testing.kotlinapplication.roomdata.ShopDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ShopRepository {
    companion object {
        var shopDatabase: ShopDatabase? = null
        var userModel: LiveData<UserModel>? = null
        var listCard: LiveData<CardModel>? = null
        var listProduct: LiveData<List<ProductsModel>>? = null
        var product: LiveData<ProductsModel>? = null
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

        fun doAddNewCart(context: Context, userId: Int) {
            CoroutineScope(IO).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.CardDAOAcess().insertCart(CardModel(userId, true))
            }
        }

        fun getCardByUserID(context: Context, userId: Int): LiveData<CardModel> {
            shopDatabase = initializeDB(context)
            listCard = shopDatabase!!.CardDAOAcess().getAllCard(userId, true)
            return listCard as LiveData<CardModel>
        }

        fun doDeletecart(context: Context, card: CardModel) {
            CoroutineScope(IO).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.CardDAOAcess().delete(card)
            }
        }

        fun doAddProductToCard(context: Context, product: ProductsModel) {
            CoroutineScope(IO).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.ProductDAOAcess().insertProuct(product)
            }
        }

        fun doGetAllProductByCardid(context: Context, id: Int): LiveData<List<ProductsModel>> {
            shopDatabase = initializeDB(context)
            listProduct = shopDatabase!!.ProductDAOAcess().getAllProductByIdCart(id)
            return listProduct as LiveData<List<ProductsModel>>
        }

        fun doDeletProduct(context: Context, product: ProductsModel) {
            CoroutineScope(IO).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.ProductDAOAcess().deleteProduct(product)
            }
        }

        fun doUpdateProduct(context: Context, data: ProductsModel) {
            CoroutineScope(IO).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.ProductDAOAcess().updateData(data)
            }
        }

        fun doCheckProduct(context: Context, data: ProductsModel): LiveData<ProductsModel> {
            shopDatabase = initializeDB(context)
            product =
                shopDatabase!!.ProductDAOAcess().checkProductInCart(data.idServer, data.idCart)
            return product as LiveData<ProductsModel>
        }

        fun doDeleteUserByID(context: Context, id: Int) {
            CoroutineScope(IO).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.ShopDAO().deletUser(id)
            }
        }

        fun doDeleteProductByID(context: Context, id: Int) {
            CoroutineScope((IO)).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.ProductDAOAcess().deleteByID(id)
            }
        }

        fun doDeactiveCartByID(context: Context, id: Int) {
            CoroutineScope(IO).launch {
                shopDatabase = initializeDB(context)
                shopDatabase!!.CardDAOAcess().doDeactiveCart(id)
            }
        }

    }
}