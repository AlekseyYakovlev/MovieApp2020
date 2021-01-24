package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.*
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieCertification

@Dao
interface MovieCertificationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieCertification: MovieCertification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieCertifications: List<MovieCertification>)

    @Query("SELECT * FROM movies_certifications WHERE movieId = :movieId")
    suspend fun getCertificationById(movieId: Int): MovieCertification?

    @Query("DELETE FROM movies_certifications")
    suspend fun clear()

    @Query("DELETE FROM movies_certifications WHERE movieId = :movieId")
    suspend fun removeById(movieId: Int)

    @Delete
    suspend fun remove(movieCertification: MovieCertification)
}