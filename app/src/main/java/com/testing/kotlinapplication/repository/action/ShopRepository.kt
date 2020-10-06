package com.testing.kotlinapplication.repository.action

import android.content.Context
import android.util.Base64
import androidx.lifecycle.LiveData
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
            context: Context,
            userName: String,
            password: String,
            email: String,
            address: String,
            type: Int
        ) {
            shopDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                val encodeString: String =
                    Base64.encodeToString(password.toByteArray(), Base64.DEFAULT)
                val userModel = UserModel(userName, encodeString, type, email, address)
                shopDatabase!!.ShopDAO().insertData(userModel)
            }
        }

        fun checkUser(context: Context, userName: String): LiveData<UserModel>? {
            shopDatabase = initializeDB(context)
            userModel = shopDatabase!!.ShopDAO().findByName(userName)
            return userModel
        }
    }
}