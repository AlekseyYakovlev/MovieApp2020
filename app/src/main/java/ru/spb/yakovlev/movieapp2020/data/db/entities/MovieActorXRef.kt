package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_actor_x_refs")
class MovieActorXRef(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val movieId: Int = 0,
    val actorId: Int = 0,
)