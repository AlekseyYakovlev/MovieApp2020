package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MovieDetailsResponse
import ru.spb.yakovlev.movieapp2020.model.ApiKey
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import ru.spb.yakovlev.movieapp2020.model.MovieDetailsData
import javax.inject.Inject
import kotlin.math.roundToInt

interface IMovieDetailsRepo {
    suspend fun getMoveDetailsById(movieId: Int, language: String): MovieDetailsData
}

class MovieDetailsRepo @Inject constructor(
    private val network: RestService,
    apiKey: ApiKey,
    private val apiSettings: ApiSettings,
) : IMovieDetailsRepo {
    private val key = apiKey.value

    override suspend fun getMoveDetailsById(movieId: Int, language: String): MovieDetailsData {
        return network.getMovieDetails(
            movieId = movieId,
            apiKey = key,
            language = language,
        ).toMovieDetailsData()
    }

    private fun MovieDetailsResponse.toMovieDetailsData() =
        MovieDetailsData(
            id = id,
            title = title,
            genre = genres.map { it.name }.reduce { acc, genre -> "$acc, $genre" },
            runtime = runtime,
            minimumAge = if (isAdult) "18+" else "13+",
            voteAverage = voteAverage.roundRating(),
            numberOfRatings = voteCount,
            backdrop = "${apiSettings.secureBaseUrl}${apiSettings.backdropSize}$backdropPath",
            isLike = false,
            overview = overview,
        )

    private fun Double.roundRating() = this.roundToInt().toFloat() / 2
}