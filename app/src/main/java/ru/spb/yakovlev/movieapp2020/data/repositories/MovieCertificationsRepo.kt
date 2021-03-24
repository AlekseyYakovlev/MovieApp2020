package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.db.daos.MovieCertificationsDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieCertification
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.err.ApiError
import ru.spb.yakovlev.movieapp2020.data.remote.err.NoNetworkError
import ru.spb.yakovlev.movieapp2020.model.DataState
import timber.log.Timber
import javax.inject.Inject

class MovieCertificationsRepo @Inject constructor(
    private val network: RestService,
    private val movieCertificationsDao: MovieCertificationsDao,
) {
    suspend fun loadCertificationForId(movieId: Int, country: String): DataState<String> {
        val certification = movieCertificationsDao.getCertificationById(movieId)

        if (certification?.country != country) {
            movieCertificationsDao.removeById(movieId)
            val movieReleaseDatesResponse = try {
                network.getMovieReleaseDates(movieId)
            } catch (e: ApiError) {
                return DataState.Error(e.message)
            } catch (e: NoNetworkError) {
                return DataState.Error(e.message)
            }
            val cert = movieReleaseDatesResponse
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

            return DataState.Success(cert?.certification ?: "")
        } else return DataState.Success(certification.certification)
    }

    suspend fun getMoviesIdsWithoutCertification(): List<Int> =
        movieCertificationsDao.getMoviesIdsWithoutCertification()
}