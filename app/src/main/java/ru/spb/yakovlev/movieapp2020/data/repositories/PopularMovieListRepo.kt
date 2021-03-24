package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.data.db.AppDb
import ru.spb.yakovlev.movieapp2020.data.db.daos.MovieDataDao
import ru.spb.yakovlev.movieapp2020.data.db.daos.PopularMoviesDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDataEntity
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieGenreXRef
import ru.spb.yakovlev.movieapp2020.data.db.entities.PopularMovieDbView
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MovieResponse
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MoviesListResponse
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

private const val NETWORK_PAGE_SIZE = 20

class PopularMovieListRepo @Inject constructor(
    private val moviesListRemoteMediator: MoviesListRemoteMediator,
    private val popularMoviesDao: PopularMoviesDao,
    private val moviesDao: MovieDataDao,
    private val network: RestService,
    private val apiSettings: ApiSettings,
    private val db: AppDb,
) {

    @ExperimentalPagingApi
    fun getPopularMoviesStream(
        language: String,
        country: String
    ): Flow<PagingData<PopularMovieDbView>> {
        val pagingSourceFactory = { popularMoviesDao.popularMovies(language) }

        moviesListRemoteMediator.also {
            it.lang = language
            it.country = country
            it.toMovieData = movieResponseToMovieData
        }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE * 3,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                enablePlaceholders = true
            ),
            remoteMediator = moviesListRemoteMediator,
            pagingSourceFactory = pagingSourceFactory,
        ).flow
    }

    suspend fun loadPopularMoviesFromNetAndSaveToDb(
        numberOfPages: Int,
        lang: String,
        country: String
    ) {
        (1..numberOfPages).forEach { page ->
            loadPageFromNet(page, lang, country)
                ?.let { parseResponse(it, page, lang) }
                ?.let { savePageToDb(it.first, it.second) }
        }
    }

    suspend fun getPosterPaths(lang: String): List<String> =
        moviesDao.getPosterPaths(lang)

    private suspend fun loadPageFromNet(
        page: Int,
        lang: String,
        country: String
    ): MoviesListResponse? =
        try {
            network.getPopular(
                page = page,
                language = lang,
                region = country
            )
        } catch (e: Exception) {
            Timber.e("PopularMovieListRepo loadPageFromNet Exception: ${e.message}")
            null
        }

    private fun parseResponse(
        response: MoviesListResponse,
        page: Int,
        lang: String,
    ): Pair<List<MovieDataEntity>, List<MovieGenreXRef>> {
        val moviesList = response.movies.map { it.movieResponseToMovieData(page, lang) }
        val xRefs = response.movies.flatMap { movie ->
            movie.genreIds.map { genreId -> MovieGenreXRef(movie.id, genreId) }
        }
        return moviesList to xRefs
    }

    private suspend fun savePageToDb(
        moviesList: List<MovieDataEntity>,
        xRefs: List<MovieGenreXRef>
    ) {
        db.withTransaction {
            db.movieDataDao().insertAll(moviesList)
            db.movieGenreXRefDao().insertAll(xRefs)
        }
    }

    private val movieResponseToMovieData: MovieResponse.(Int, String) -> MovieDataEntity =
        { page, lang ->
            MovieDataEntity(
                id = id,
                title = title,
                voteAverage = voteAverage.roundRating(),
                numberOfRatings = voteCount,
                poster = "${apiSettings.secureBaseUrl}${apiSettings.posterSize}${posterPath}",
                language = lang,
                page = page
            )
        }

    private fun Double.roundRating() = this.roundToInt().toFloat() / 2
}