package com.testing.kotlinapplication.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserModel(
    @ColumnInfo(name = "username")
    var UserName: String,
    @ColumnInfo(name = "password")
    var Password: String,
    @ColumnInfo(name = "usertype")
    var userType: Int,
    @ColumnInfo(name = "email")
    var email:String,
    @ColumnInfo(name = "address")
    var address: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null
}