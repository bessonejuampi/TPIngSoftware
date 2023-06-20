package com.example.tpingsoftware.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.PropertyKey

@Entity(tableName = "user_table")
class User(
    var id : Int = 0
) {
   @PrimaryKey(autoGenerate = true) var ids: Int = 0
}