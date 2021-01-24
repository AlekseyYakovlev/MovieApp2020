package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.db.daos.MovieCertificationsDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieCertification
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import timber.log.Timber
import javax.inject.Inject

class MovieCertificationsRepo @Inject constructor(
    private val network: RestService,
    private val movieCertificationsDao: MovieCertificationsDao,
) {
    suspend fun loadCertificationForId(movieId: Int, country: String): String {
        val certification = movieCertificationsDao.getCertificationById(movieId)

        return if (certification?.country != country) {
            Timber.tag("12345678")
                .d("certification?.country = ${certification?.country} country = $country")
            movieCertificationsDao.removeById(movieId)
            val cert = network.getMovieReleaseDates(movieId)
                .results
                .find { it.country == country }
                ?.let {
                    MovieCertification(
                        movieId,
                        it.releaseDates.firstOrNull()?.certification ?: "",
                        country
                    )
                }
            cert?.let { movieCertificationsDao.insert(it) }

            cert?.certification ?: ""
        } else certification.certification
    }
}