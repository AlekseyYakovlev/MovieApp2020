package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actors")
class ActorEntity(
    @PrimaryKey val id: Int = 0,
    val name: String = "",
    val photo: String = "",
    val language: String = "",
)