package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieActorXRef

@Dao
interface MovieActorXRefsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(xRefs: List<MovieActorXRef>)

    @Query("DELETE FROM movie_actor_x_refs")
    suspend fun clear()

    @Query("SELECT uid FROM movie_actor_x_refs WHERE movieId = :movieId LIMIT 1")
    suspend fun getFirstUidForMovieId(movieId: Int): Int?
}