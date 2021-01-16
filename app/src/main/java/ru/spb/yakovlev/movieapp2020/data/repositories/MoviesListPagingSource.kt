package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.paging.PagingSource
import retrofit2.HttpException
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MovieResponse
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import ru.spb.yakovlev.movieapp2020.model.Locale
import ru.spb.yakovlev.movieapp2020.model.MovieData
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

private const val STARTING_PAGE_INDEX = 1

class MoviesListPagingSource @Inject constructor(
    private val network: RestService,
    private val apiSettings: ApiSettings,
    locale: Locale,
) : PagingSource<Int, MovieData>() {

    private val lang = locale.name
    private val region = locale.country

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = network.getPopular(
                page = position,
                language = lang,
                region = region
            )
            val moviesList = response.movies.map { it.toMovieData() }
            val nextKey = if (response.page >= response.totalPages) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = moviesList,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private fun MovieResponse.toMovieData() =
        MovieData(
            id = id,
            title = title,
            genres = genreIds,
            runtime = 0,
            minimumAge = if (isAdult) "18+" else "13+",
            voteAverage = voteAverage.roundRating(),
            numberOfRatings = voteCount,
            poster = "${apiSettings.secureBaseUrl}${apiSettings.posterSize}$posterPath",
            backdrop = backdropPath,
            isLike = false,
            overview = overview,
        )

    private fun Double.roundRating() = this.roundToInt().toFloat() / 2
}