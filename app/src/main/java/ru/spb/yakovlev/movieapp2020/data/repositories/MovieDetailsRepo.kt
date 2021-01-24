package ru.spb.yakovlev.movieapp2020.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.movieapp2020.data.db.daos.MovieDetailsDao
import ru.spb.yakovlev.movieapp2020.data.db.daos.MovieDetailsFullDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDetailsEntity
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDetailsFullDbView
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MovieDetailsResponse
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import ru.spb.yakovlev.movieapp2020.model.MovieDetailsData
import javax.inject.Inject
import kotlin.math.roundToInt


class MovieDetailsRepo @Inject constructor(
    private val network: RestService,
    private val movieDetailsDao: MovieDetailsDao,
    private val movieDetailsFullDao: MovieDetailsFullDao,
    private val apiSettings: ApiSettings,
) {
    suspend fun getMoveDetailsById(
        movieId: Int,
        language: String,
    ): Flow<MovieDetailsData> {
        loadMoveDetails(movieId, language)
        return movieDetailsFullDao.getMovieDetailsFull(movieId).map { it.toMovieDetailsData() }
    }

    suspend fun getRuntime(movieId: Int, language: String) =
        loadMoveDetails(movieId, language).runtime


    private suspend fun loadMoveDetails(movieId: Int, language: String): MovieDetailsEntity {
        val movieDetailsEntity = movieDetailsDao.getMovieDetailsById(movieId)
        return if (movieDetailsEntity?.language != language) {
            val movieDetails = network.getMovieDetails(
                movieId = movieId,
                language = language,
            ).toMovieDetailsEntity(language)
            movieDetails.let { movieDetailsDao.insert(it) }
            movieDetails
        } else movieDetailsEntity
    }

    private fun MovieDetailsResponse.toMovieDetailsEntity(language: String): MovieDetailsEntity {
        val backdrop = if (backdropPath.isNotBlank()) backdropPath else posterPath
        return MovieDetailsEntity(
            id = id,
            title = title,
            genre = genres.map { it.name }.reduce { acc, genre -> "$acc, $genre" },
            runtime = runtime,
            voteAverage = voteAverage.roundRating(),
            numberOfRatings = voteCount,
            backdrop = backdrop,
            overview = overview,
            language = language
        )
    }

    private fun MovieDetailsFullDbView.toMovieDetailsData() = MovieDetailsData(
        id = id,
        title = title,
        genre = genre,
        runtime = runtime,
        certification = certification ?: "",
        voteAverage = voteAverage,
        numberOfRatings = numberOfRatings,
        backdrop = "${apiSettings.secureBaseUrl}${apiSettings.backdropSize}$backdrop",
        isLiked = isLiked,
        overview = overview
    )

    private fun Double.roundRating() = this.roundToInt().toFloat() / 2
}