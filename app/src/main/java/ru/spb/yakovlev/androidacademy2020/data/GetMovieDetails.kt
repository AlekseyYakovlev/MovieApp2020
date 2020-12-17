package ru.spb.yakovlev.androidacademy2020.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.spb.yakovlev.androidacademy2020.model.ActorItemData
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieData
import ru.spb.yakovlev.androidacademy2020.model.MovieDetailsData

class GetMovieDetailsState(private val movieId: Int) {
    private val _state = MutableStateFlow<DataState<MovieDetailsData>>(DataState.Empty)
    val state: StateFlow<DataState<MovieDetailsData>> = _state

    private val moviesRepo = MovieListMokkRepo
    private val actorsRepo = ActorsListMokkRepo

    suspend fun update() {
        val movie = moviesRepo.getMovieById(movieId)
        _state.value = DataState.Success(movie.toMovieMovieDetailsData(emptyList()))
        _state.value = DataState.Loading(null)
        val actors = actorsRepo.getActorItemsById(movie.castIds)
        delay(1000)
        _state.value = DataState.Success(movie.toMovieMovieDetailsData(actors))
    }

    fun handleLike(){
        val movie = moviesRepo.getMovieById(movieId)
        val actors = actorsRepo.getActorItemsById(movie.castIds)
        _state.value = DataState.Success(movie.toMovieMovieDetailsData(actors))
    }


    private fun MovieData.toMovieMovieDetailsData(actors: List<ActorItemData>) = MovieDetailsData(
        id = id,
        title = title,
        genre = genres,
        runtime = runtime,
        minimumAge = minimumAge,
        ratings = rating,
        numberOfRatings = numberOfRatings,
        backdrop = poster2,
        isLike = isLike,
        overview = overview,
        actorItemsData = actors,
    )
}