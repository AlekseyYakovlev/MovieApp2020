package ru.spb.yakovlev.movieapp2020.model.interactors

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spb.yakovlev.movieapp2020.data.db.entities.PopularMovieDbView
import ru.spb.yakovlev.movieapp2020.data.repositories.GenresRepo
import ru.spb.yakovlev.movieapp2020.data.repositories.MovieListRepo
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import javax.inject.Inject

class GetMoviesListPopularUseCase @Inject constructor(
    private val genresRepo: GenresRepo,
    private val movieListRepo: MovieListRepo,
) : IUseCase {

    @OptIn(ExperimentalPagingApi::class)
    suspend operator fun invoke(
        language: String,
        country: String
    ): Flow<PagingData<MovieItemData>> {
        withContext(Dispatchers.IO) {
            launch { genresRepo.loadGenres(language) }
        }
        return withContext(Dispatchers.IO) {
            movieListRepo.getPopularMoviesStream(language, country).map { pagingData ->
                pagingData.map {
                    it.toMovieItemData()
                }
            }
        }
    }

    private fun PopularMovieDbView.toMovieItemData() =
        MovieItemData(
            uid = uid,
            id = id,
            title = title,
            genre = genre ?: "",
            voteAverage = voteAverage,
            numberOfRatings = numberOfRatings,
            poster = poster,
            isLike = isLiked,
        )
}