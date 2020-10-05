package com.testing.kotlinapplication.roomdata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.testing.kotlinapplication.repository.UserModel

@Dao
interface DAOAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(userModel: UserModel)

    @Query("SELECT * FROM User WHERE username LIKE :name")
    fun findByName(name: String): LiveData<UserModel>
}