package ru.spb.yakovlev.movieapp2020.model.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.spb.yakovlev.movieapp2020.data.repositories.ActorsRepo
import ru.spb.yakovlev.movieapp2020.model.ActorData
import ru.spb.yakovlev.movieapp2020.model.ActorItemData
import ru.spb.yakovlev.movieapp2020.model.DataState
import javax.inject.Inject

class GetMovieDetailsCast @Inject constructor(
    private val actorsRepo: ActorsRepo,
) : IUseCase {
    suspend operator fun invoke(
        movieId: Int,
        language: String,
    ): Flow<DataState<List<ActorItemData>>> = flow {
        emit(DataState.Loading(0))
        emit(
            actorsRepo.getCastByMovieId(movieId, language)
                .let { dataState ->
                    when (dataState) {
                        is DataState.Success -> DataState.Success(dataState.data.map { it.toActorItemData() })
                        is DataState.Error -> DataState.Error(dataState.errorMessage)
                        else -> DataState.Empty
                    }
                }
        )
    }
}

fun ActorData.toActorItemData() = ActorItemData(
    id = id,
    name = name,
    photo = photo,
)