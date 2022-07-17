package com.anushka.roomapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "books_table")
data class Book(

        @PrimaryKey
        var id: String,

        var date: String,
        var start: String,

        @ColumnInfo(name = "published_author")
        var end: String
)
