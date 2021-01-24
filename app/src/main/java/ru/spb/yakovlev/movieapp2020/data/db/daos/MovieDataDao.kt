package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDataEntity

@Dao
interface MovieDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<MovieDataEntity>)

    @Query("SELECT * FROM movies")
    fun popularMovies(): PagingSource<Int, MovieDataEntity>

    @Query("DELETE FROM movies")
    suspend fun clear()

    @Query("SELECT language FROM movies LIMIT 1")
    fun getLanguage(): String
}