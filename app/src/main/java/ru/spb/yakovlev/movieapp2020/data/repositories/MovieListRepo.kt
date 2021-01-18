package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.data.db.MovieDataDao
import ru.spb.yakovlev.movieapp2020.model.MovieData
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 20

interface IMovieListRepo {
    suspend fun getPopularMoviesStream(): Flow<PagingData<MovieData>>
}

class MovieListRepo @Inject constructor(
    private val moviesListRemoteMediator: MoviesListRemoteMediator,
    private val movieDataDao: MovieDataDao,
) : IMovieListRepo {

    @ExperimentalPagingApi
    override suspend fun getPopularMoviesStream(): Flow<PagingData<MovieData>> {
        val pagingSourceFactory = { movieDataDao.popularMovies() }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = NETWORK_PAGE_SIZE * 2,
                maxSize = NETWORK_PAGE_SIZE * 20,
                enablePlaceholders = true
            ),
            remoteMediator = moviesListRemoteMediator,
            pagingSourceFactory = pagingSourceFactory,
        ).flow
    }
}