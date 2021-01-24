package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "movie_genre_x_refs",
    primaryKeys = ["movie_id", "genre_id"],
    foreignKeys = [
        ForeignKey(
            entity = MovieDataEntity::class,
            parentColumns = ["id"],
            childColumns = ["movie_id"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["movie_id", "genre_id"], unique = true)]
)
class MovieGenreXRef(
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "genre_id")
    val genreId: Int
)