package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.*
import ru.spb.yakovlev.movieapp2020.data.db.entities.GenreEntity

@Dao
interface GenresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<GenreEntity>)

    @Query("SELECT language FROM genres LIMIT 1")
    suspend fun language(): String

    @Query("DELETE FROM genres")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(genres: List<GenreEntity>) {
        clear()
        insertAll(genres)
    }
}