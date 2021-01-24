package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.*
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDetailsEntity

@Dao
interface MovieDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieDetailsEntity: MovieDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieDetailsEntities: List<MovieDetailsEntity>)

    @Query("SELECT * FROM movies_details WHERE id = :movieId")
    suspend fun getMovieDetailsById(movieId:Int): MovieDetailsEntity?

    @Query("DELETE FROM movies_details")
    suspend fun clear()

    @Delete
    suspend fun remove(movieDetailsEntity: MovieDetailsEntity)
}