package ru.spb.yakovlev.movieapp2020.data.db.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDetailsFullDbView

@Dao
interface MovieDetailsFullDao {
    @Query("SELECT * FROM MovieDetailsFullDbView WHERE id = :movieId")
    fun getMovieDetailsFull(movieId: Int): Flow<MovieDetailsFullDbView>
}