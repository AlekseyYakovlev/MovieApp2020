package ru.spb.yakovlev.movieapp2020.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.spb.yakovlev.movieapp2020.model.MovieData

@Dao
interface MovieDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<MovieData>)

    @Query("SELECT * FROM movies")
    fun popularMovies(): PagingSource<Int, MovieData>

    @Query("DELETE FROM movies")
    suspend fun clearRepos()
}