package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.*
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDetailsEntity

@Dao
interface MovieDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieDetailsEntity: MovieDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieDetailsEntities: List<MovieDetailsEntity>)

    @Query("SELECT * FROM movies_details WHERE id = :movieId AND language = :language LIMIT 1")
    suspend fun getMovieDetailsById(movieId:Int, language: String): MovieDetailsEntity?

    @Query("DELETE FROM movies_details")
    suspend fun clear()

    @Delete
    suspend fun remove(movieDetailsEntity: MovieDetailsEntity)

    @Query("""
        SELECT m.id
        FROM movies AS m 
        LEFT JOIN movies_details AS d ON m.id = d.id
        WHERE d.id IS NULL
    """)
    suspend fun getMoviesIdsWithoutDetails(): List<Int>
}