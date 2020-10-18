package com.testing.kotlinapplication.roomdata

import androidx.lifecycle.LiveData
import androidx.room.*
import com.testing.kotlinapplication.repository.CardModel
import com.testing.kotlinapplication.repository.ProductsModel

@Dao
interface ProductDAOAcess {

    //Query for Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProuct(productsModel: ProductsModel)

    @Query("select * from Product where idCart LIKE :id ")
    fun getAllProductByIdCart(id: Int): LiveData<List<ProductsModel>>

    @Delete
    fun deleteProduct(productsModel: ProductsModel)

    @Delete
    fun deleateAllProduct(mList: ArrayList<ProductsModel>)
}