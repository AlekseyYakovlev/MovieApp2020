package ru.spb.yakovlev.movieapp2020.model.interactors

import ru.spb.yakovlev.movieapp2020.data.repositories.MovieDetailsRepo
import javax.inject.Inject

class GetMovieRuntimeUseCase @Inject constructor(
    private val movieDetailsRepo: MovieDetailsRepo,
) : IUseCase {
    suspend operator fun invoke(
        movieId: Int,
        language: String,
    ): Int =
        movieDetailsRepo.getRuntime(movieId, language)
}