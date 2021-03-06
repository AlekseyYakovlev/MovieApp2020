package ru.spb.yakovlev.movieapp2020.model.interactors

import ru.spb.yakovlev.movieapp2020.data.repositories.MovieCertificationsRepo
import ru.spb.yakovlev.movieapp2020.model.DataState
import javax.inject.Inject

class GetCertificationUseCase @Inject constructor(
    private val movieCertificationsRepo: MovieCertificationsRepo,
) : IUseCase {
    suspend operator fun invoke(
        movieId: Int,
        country: String,
    ): String {
        val state = movieCertificationsRepo.loadCertificationForId(movieId, country)
        return if (state is DataState.Success) state.data
        else ""
    }
}