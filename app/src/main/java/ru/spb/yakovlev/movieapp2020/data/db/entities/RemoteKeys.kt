package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)