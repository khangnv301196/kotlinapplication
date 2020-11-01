package com.testing.kotlinapplication.roomdata

import androidx.lifecycle.LiveData
import androidx.room.*
import com.testing.kotlinapplication.repository.CardModel

@Dao
interface CardDAOAcess {
    //Query for Cart
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CardModel): Long

    @Query("SELECT * FROM Card WHERE user_id LIKE :id AND status like :status")
    fun getAllCard(id: Int, status: Boolean): LiveData<CardModel>

    @Update
    fun Update(cart: CardModel)

    @Delete
    fun delete(cart: CardModel)

    @Query("Update Card set status = 0 where id like :id")
    fun doDeactiveCart(id: Int)

}