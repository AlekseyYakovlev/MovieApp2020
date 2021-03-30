package ru.spb.yakovlev.movieapp2020.model.interactors

import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.data.repositories.MovieDetailsRepo
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.MovieDetailsData
import javax.inject.Inject

class GetMovieDetailsStateUseCase @Inject constructor(
    private val movieDetailsRepo: MovieDetailsRepo,
) : IUseCase {
    suspend operator fun invoke(
        movieId: Int,
        language: String,
    ): Flow<DataState<MovieDetailsData>> =
        movieDetailsRepo.getMoveDetailsById(movieId, language)
}