package com.example.shoesstore.model.UserModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var firstName: String = "",

    var lastName: String = "",

    var userName: String = "",

    var password: String = ""

)
