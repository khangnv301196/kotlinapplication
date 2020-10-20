package com.testing.kotlinapplication.repository

import android.view.Display
import androidx.room.*

@Entity(tableName = "Product")
data class ProductsModel(
    @ColumnInfo(name = "name")
    var productName: String,
    @ColumnInfo(name = "idCart")
    var idCart: Int,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "id_server")
    var idServer: Int,
    @ColumnInfo(name = "total")
    var Total: Int,
    @ColumnInfo(name = "price")
    var price: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null
}