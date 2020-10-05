package com.testing.kotlinapplication.roomdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.testing.kotlinapplication.repository.UserModel

@Database(entities = arrayOf(UserModel::class), version = 1)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun ShopDAO(): DAOAccess

    companion object {
        @Volatile
        private var INSTANCE: ShopDatabase? = null
        fun getDatabaseClient(context: Context): ShopDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, ShopDatabase::class.java, "LOGIN_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }
    }
}