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

    @Query("select * from Product where id_server Like :idServer and idCart like :idCart")
    fun checkProductInCart(idServer: Int, idCart: Int): LiveData<ProductsModel>

    @Update
    fun updateData(productsModel: ProductsModel)

    @Delete
    fun deleteProduct(productsModel: ProductsModel)

    @Delete
    fun deleateAllProduct(mList: ArrayList<ProductsModel>)

    @Query("Delete from Product where idCart like :id")
    fun deleteByID(id: Int)
}