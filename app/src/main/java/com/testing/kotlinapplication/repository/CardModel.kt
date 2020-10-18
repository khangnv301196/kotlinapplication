package com.testing.kotlinapplication.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Card")
data class CardModel(
    @ColumnInfo(name = "user_id")
    var user_id: Int,
    @ColumnInfo(name = "status")
    var status: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null
}