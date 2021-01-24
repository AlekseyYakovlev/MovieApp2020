package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_certifications")
class MovieCertification(
    @PrimaryKey
    val movieId: Int = 0,
    val certification: String = "",
    val country: String = "",
)