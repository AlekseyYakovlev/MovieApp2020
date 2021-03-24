package ru.spb.yakovlev.movieapp2020.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.movieapp2020.data.db.daos.MovieDetailsDao
import ru.spb.yakovlev.movieapp2020.data.db.daos.MovieDetailsFullDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDetailsEntity
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDetailsFullDbView
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.err.ApiError
import ru.spb.yakovlev.movieapp2020.data.remote.err.NoNetworkError
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MovieDetailsResponse
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import ru.spb.yakovlev.movieapp2020.model.DataState
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
    ): Flow<DataState<MovieDetailsData>> {
        if (!isDataInDb(movieId, language)) {
            val loadingResult = loadMoveDetails(movieId, language)
            if (loadingResult is DataState.Error) {
                return flow { emit(DataState.Error(loadingResult.errorMessage)) }
            }
        }

        return movieDetailsFullDao.getMovieDetailsFull(movieId)
            .filterNotNull()
            .map { DataState.Success(it.toMovieDetailsData()) }
    }

    suspend fun getRuntime(movieId: Int, language: String): Int {
        return if (isDataInDb(movieId, language)) {
            movieDetailsDao.getMovieDetailsById(movieId, language)?.runtime ?: 0
        } else {
            val loadingResult = loadMoveDetails(movieId, language)

            if (loadingResult is DataState.Success) {
                loadingResult.data.runtime
            } else 0
        }
    }


    suspend fun getMoviesIdsWithoutDetails() =
        movieDetailsDao.getMoviesIdsWithoutDetails()


    suspend fun loadMoveDetails(movieId: Int, language: String): DataState<MovieDetailsEntity> {
        val movieDetailsFromNet = try {
            getFromNet(movieId, language)
        } catch (e: ApiError) {
            return DataState.Error(e.message)
        } catch (e: NoNetworkError) {
            return DataState.Error(e.message)
        }

        val movieDetails = movieDetailsFromNet.toMovieDetailsEntity(language)
        saveToDb(movieDetails)

        return DataState.Success(movieDetails)
    }


    private suspend fun isDataInDb(movieId: Int, language: String): Boolean {
        val movieDetailsEntity = movieDetailsDao.getMovieDetailsById(movieId, language)
        return (movieDetailsEntity != null)
    }

    private suspend fun getFromNet(movieId: Int, language: String): MovieDetailsResponse =
        network.getMovieDetails(
            movieId = movieId,
            language = language,
        )

    private suspend fun saveToDb(movieDetailsEntity: MovieDetailsEntity) {
        movieDetailsDao.insert(movieDetailsEntity)
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

    private fun MovieDetailsFullDbView.toMovieDetailsData() =
        MovieDetailsData(
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