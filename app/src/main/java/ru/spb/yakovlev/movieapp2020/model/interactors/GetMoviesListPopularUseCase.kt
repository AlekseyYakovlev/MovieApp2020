package ru.spb.yakovlev.movieapp2020.model.interactors

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.movieapp2020.data.db.entities.PopularMovieDbView
import ru.spb.yakovlev.movieapp2020.data.repositories.PopularMovieListRepo
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import javax.inject.Inject

class GetMoviesListPopularUseCase @Inject constructor(
    private val popularMovieListRepo: PopularMovieListRepo,
) : IUseCase {
    @OptIn(ExperimentalPagingApi::class)
    operator fun invoke(
        language: String,
        country: String
    ): Flow<PagingData<MovieItemData>> =
        popularMovieListRepo.getPopularMoviesStream(language, country)
            .map { pagingData ->
                pagingData.map {
                    it.toMovieItemData()
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