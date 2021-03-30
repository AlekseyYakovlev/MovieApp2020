package ru.spb.yakovlev.movieapp2020.model.interactors

import ru.spb.yakovlev.movieapp2020.data.repositories.MovieDetailsRepo
import javax.inject.Inject

class PreloadMoviesDetails  @Inject constructor(
private  val movieDetailsRepo: MovieDetailsRepo,
) : IUseCase {
    suspend operator fun invoke(language: String) {
        val ids = movieDetailsRepo.getMoviesIdsWithoutDetails()
        ids.forEach { movieDetailsRepo.loadMoveDetails(it, language) }
    }
}