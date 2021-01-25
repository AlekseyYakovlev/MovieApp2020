package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.spb.yakovlev.movieapp2020.data.db.entities.ActorEntity

@Dao
interface ActorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(actors: List<ActorEntity>)

    @Query("DELETE FROM actors")
    suspend fun clear()

    @Query(
        """
        SELECT a.* 
        FROM actors as a, movie_actor_x_refs as ref
        WHERE ref.movieId = :movieId AND a.id = ref.actorId
        ORDER BY ref.castId
        """
    )
    suspend fun getActorsByMovieId(movieId: Int): List<ActorEntity>
}