package ru.spb.yakovlev.androidacademy2020.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.spb.yakovlev.androidacademy2020.data.MovieListDummyRepo
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieDetailsData
import ru.spb.yakovlev.androidacademy2020.model.toMovieMovieDetailsData

class MovieDetailsViewModel : ViewModel() {
    private val _movieDetailsState = MutableStateFlow<DataState<MovieDetailsData>>(DataState.Empty)
    val movieDetailsState: StateFlow<DataState<MovieDetailsData>> = _movieDetailsState

    private var movieId = 0

    fun setMovieId(id: Int) {
        movieId = id
        _movieDetailsState.value = DataState.Loading(null)
        _movieDetailsState.value = DataState.Success<MovieDetailsData>(
            MovieListDummyRepo.getMovieById(movieId).toMovieMovieDetailsData()
        )
    }

    fun handleLike(movieId: Int, isLike: Boolean) {
        MovieListDummyRepo.updateMovieLike(movieId, isLike)
        _movieDetailsState.value = DataState.Success<MovieDetailsData>(
            MovieListDummyRepo.getMovieById(movieId).toMovieMovieDetailsData()
        )
    }
}