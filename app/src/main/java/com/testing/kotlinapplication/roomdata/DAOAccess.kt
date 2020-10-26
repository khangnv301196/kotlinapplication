package com.testing.kotlinapplication.roomdata

import androidx.lifecycle.LiveData
import androidx.room.*
import com.testing.kotlinapplication.network.model.User
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel
import com.testing.kotlinapplication.repository.UserModel

@Dao
interface DAOAccess {

    //Querry for User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(userModel: UserModel)

    @Query("SELECT * FROM User WHERE TenDangNhap LIKE :name")
    fun findByName(name: String): LiveData<UserModel>

    @Query("SELECT * FROM User WHERE user_id LIKE:id")
    fun findByID(id: Int): LiveData<UserModel>

    @Query("Delete from User Where user_id like :id")
    fun deletUser(id: Int)
}
