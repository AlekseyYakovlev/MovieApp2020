package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["uid", "id", "language"], unique = true)
    ]
)
data class MovieDataEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val id: Int = 0,
    val title: String = "",
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val poster: String = "",
    val language: String = "",
    val page: Int = 0,
)