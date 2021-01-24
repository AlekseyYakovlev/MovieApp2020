package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieGenreXRef
import ru.spb.yakovlev.movieapp2020.model.Genre

@Dao
interface MovieGenreXRefsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(xRef: MovieGenreXRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(xRefs: List<MovieGenreXRef>)

    @Query("DELETE FROM movie_genre_x_refs")
    suspend fun clear()
}