package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val language: String,
)