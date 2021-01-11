package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.model.MovieData
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 20

interface IMovieListRepo {
    fun getPopularMoviesStream(): Flow<PagingData<MovieData>>
//TODO add following functions:
//    suspend fun getNowPlayingMovies(language: String): List<MovieData>
//    suspend fun getTopRatedMovies(language: String): List<MovieData>
//    suspend fun getUpcomingMovies(language: String): List<MovieData>
//    suspend fun getMovieDetails(language: String): List<MovieData>
//    suspend fun searchMovie(
//        query: String,
//        includeAdult: Boolean = false,
//        year: Int? = null,
//        primaryReleaseYear: Int? = null,
//    )
}

class MovieListRepo @Inject constructor(
    private val moviesListPagingSource: MoviesListPagingSource,
) : IMovieListRepo {

    override fun getPopularMoviesStream(): Flow<PagingData<MovieData>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { moviesListPagingSource },
        ).flow
    }
}