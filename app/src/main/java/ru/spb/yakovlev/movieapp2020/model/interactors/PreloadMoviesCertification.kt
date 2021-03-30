package ru.spb.yakovlev.movieapp2020.model.interactors

import ru.spb.yakovlev.movieapp2020.data.repositories.MovieCertificationsRepo
import timber.log.Timber
import javax.inject.Inject

class PreloadMoviesCertification @Inject constructor(
    private val movieCertificationsRepo: MovieCertificationsRepo,
) : IUseCase {
    suspend operator fun invoke(country: String) {
        val ids = movieCertificationsRepo.getMoviesIdsWithoutCertification()
        ids.forEach { movieCertificationsRepo.loadCertificationForId(it, country) }
    }
}