package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import ru.spb.yakovlev.movieapp2020.data.db.entities.PopularMovieDbView

@Dao
interface PopularMoviesDao {
    @Query("SELECT * FROM PopularMovieDbView WHERE language = :language")
    fun popularMovies(language: String): PagingSource<Int, PopularMovieDbView>

    @Query("SELECT language FROM PopularMovieDbView LIMIT 1")
    fun getLanguage(): String
}