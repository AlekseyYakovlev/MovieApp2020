package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.data.db.daos.PopularMoviesDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.PopularMovieDbView
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 20

class MovieListRepo @Inject constructor(
    private val moviesListRemoteMediator: MoviesListRemoteMediator,
    private val popularMoviesDao: PopularMoviesDao
) {

    @ExperimentalPagingApi
    fun getPopularMoviesStream(
        language: String,
        country: String
    ): Flow<PagingData<PopularMovieDbView>> {
        val pagingSourceFactory = { popularMoviesDao.popularMovies(language) }

        moviesListRemoteMediator.lang = language
        moviesListRemoteMediator.country = country

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
}