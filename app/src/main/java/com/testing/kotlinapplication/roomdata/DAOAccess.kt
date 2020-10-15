package com.testing.kotlinapplication.roomdata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testing.kotlinapplication.network.model.User
import com.testing.kotlinapplication.repository.UserModel

@Dao
interface DAOAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(userModel: UserModel)

    @Query("SELECT * FROM User WHERE TenDangNhap LIKE :name")
    fun findByName(name: String): LiveData<UserModel>

    @Query("SELECT * FROM User WHERE user_id LIKE:id")
    fun findByID(id: Int): LiveData<UserModel>
}