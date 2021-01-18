package ru.spb.yakovlev.movieapp2020.model.interactors

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.spb.yakovlev.movieapp2020.data.repositories.GenresRepo
import ru.spb.yakovlev.movieapp2020.data.repositories.MovieListRepo
import ru.spb.yakovlev.movieapp2020.model.MovieData
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import javax.inject.Inject

class GetMoviesListPopular @Inject constructor(
    private val genresRepo: GenresRepo,
    private val movieListRepo: MovieListRepo,
) : IUseCase {

    @OptIn(ExperimentalPagingApi::class)
    suspend fun getMoviesStream(): Flow<PagingData<MovieItemData>> {
        val genres =
            withContext(Dispatchers.IO) {
                genresRepo.getMovieGenres()
            }

        return withContext(Dispatchers.IO) {
            movieListRepo.getPopularMoviesStream().map { pagingData ->
                pagingData.map { movieData ->
                    val genresStr = genres.filter { it.id in movieData.genres }
                        .map { it.name }
                        .reduce { acc, genre -> "$acc, $genre" }
                    movieData.toMovieItemData(genresStr)
                }
            }
        }
    }

    private fun MovieData.toMovieItemData(genresList: String) =
        MovieItemData(
            id = id,
            title = title,
            genre = genresList,
            runtime = runtime,
            minimumAge = minimumAge,
            voteAverage = voteAverage,
            numberOfRatings = numberOfRatings,
            poster = poster,
            isLike = isLike,
        )
}