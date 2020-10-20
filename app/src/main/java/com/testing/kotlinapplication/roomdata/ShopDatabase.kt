package com.testing.kotlinapplication.roomdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.UserModel

@Database(
    entities = arrayOf(UserModel::class, CardModel::class, ProductsModel::class),
    version = 1,
    exportSchema = false
)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun ShopDAO(): DAOAccess
    abstract fun CardDAOAcess(): CardDAOAcess
    abstract fun ProductDAOAcess(): ProductDAOAcess


    companion object {
        @Volatile
        private var INSTANCE: ShopDatabase? = null
        fun getDatabaseClient(context: Context): ShopDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, ShopDatabase::class.java, "LOGIN_DATABASE")
                    .build()

                return INSTANCE!!

            }
        }
    }
}