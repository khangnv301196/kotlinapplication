package com.testing.kotlinapplication.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Product",
    foreignKeys = [
        ForeignKey(
            entity = CardModel::class,
            parentColumns = ["id"],
            childColumns = ["idCart"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductsModel(
    @ColumnInfo(name = "name")
    var productName: String,
    @ColumnInfo(name = "idCart")
    var idCart: Int,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "id_server")
    var idServer: String,
    @ColumnInfo(name = "total")
    var Total: Int,
    @ColumnInfo(name = "price")
    var price: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null
}