package ru.spb.yakovlev.movieapp2020.model.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import ru.spb.yakovlev.movieapp2020.data.repositories.MovieDetailsRepo
import ru.spb.yakovlev.movieapp2020.data.repositories.PeopleRepo
import ru.spb.yakovlev.movieapp2020.model.*
import javax.inject.Inject

class GetMovieDetailsState @Inject constructor(
    private val movieDetailsRepo: MovieDetailsRepo,
    private val peopleRepo: PeopleRepo,
    locale: Locale,
) : IUseCase {
    private val lang = locale.name

    private val _state = MutableStateFlow<DataState<MovieDetailsDataWithCast>>(DataState.Empty)
    val state: StateFlow<DataState<MovieDetailsDataWithCast>> = _state

    suspend fun update(movieId: Int) {
        coroutineScope {
            _state.value = DataState.Empty
            _state.value = DataState.Loading(null)
            withContext(Dispatchers.IO) {
                val movie = movieDetailsRepo.getMoveDetailsById(movieId, lang)
                _state.value = DataState.Success(movie.toMovieDetailsDataWithCast(emptyList()))
                val actors = peopleRepo.getCastByMovieId(movieId, lang)
                _state.value =
                    DataState.Success(movie.toMovieDetailsDataWithCast(actors.map { it.toActorItemData() }))
            }
        }
    }


    private fun MovieDetailsData.toMovieDetailsDataWithCast(actors: List<ActorItemData>) =
        MovieDetailsDataWithCast(
            id = id,
            title = title,
            genre = "",
            runtime = runtime,
            minimumAge = minimumAge,
            voteAverage = voteAverage,
            numberOfRatings = numberOfRatings,
            backdrop = backdrop,
            isLike = isLike,
            overview = overview,
            actorItemsData = actors,
        )
}