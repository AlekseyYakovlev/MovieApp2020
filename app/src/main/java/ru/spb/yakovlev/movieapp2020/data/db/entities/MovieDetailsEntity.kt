package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies_details",
    indices = [Index(value = ["id", "language"], unique = true)]
)
class MovieDetailsEntity(
    @PrimaryKey
    val id: Int = 0,
    val title: String = "",
    val genre: String = "",
    val runtime: Int = 0,
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val backdrop: String = "",
    val overview: String = "",
    val language: String = "",
)