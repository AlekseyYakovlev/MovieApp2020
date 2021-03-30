package ru.spb.yakovlev.movieapp2020.model.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.spb.yakovlev.movieapp2020.data.repositories.MovieCertificationsRepo
import ru.spb.yakovlev.movieapp2020.model.DataState
import javax.inject.Inject

class GetCertificationStateFlowUseCase @Inject constructor(
    private val movieCertificationsRepo: MovieCertificationsRepo,
) : IUseCase {
    suspend operator fun invoke(
        movieId: Int,
        country: String,
    ): Flow<DataState<String>> = flow {
        emit(DataState.Loading(0))
        emit(movieCertificationsRepo.loadCertificationForId(movieId, country))
    }
}