package ru.spb.yakovlev.movieapp2020.ui.movie_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.MovieDetailsDataWithCast
import ru.spb.yakovlev.movieapp2020.model.interactors.GetMovieDetailsState

class MovieDetailsViewModel@ViewModelInject constructor(
    private val getMovieDetailsState: GetMovieDetailsState
)
    : ViewModel() {

    private var movieId = 0

    val movieDetailsState: StateFlow<DataState<MovieDetailsDataWithCast>>
        get() {
            return when (movieId) {
                0 -> MutableStateFlow(DataState.Empty)
                else -> getMovieDetailsState.state
            }
        }

    fun setMovieId(id: Int) {
        movieId = id

        viewModelScope.launch {
            getMovieDetailsState.update(movieId)
        }
    }

    fun handleLike(isLike: Boolean) {
        // TODO Fix handleLike
    }
}