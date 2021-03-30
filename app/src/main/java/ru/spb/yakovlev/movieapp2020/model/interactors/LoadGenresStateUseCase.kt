package ru.spb.yakovlev.movieapp2020.model.interactors

import ru.spb.yakovlev.movieapp2020.data.repositories.GenresRepo
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.Genre
import javax.inject.Inject

class LoadGenresStateUseCase @Inject constructor(
    private val genresRepo: GenresRepo,
) {
    suspend operator fun invoke(
        language: String,
    ): DataState<List<Genre>> =
        genresRepo.getGenres(language)
}