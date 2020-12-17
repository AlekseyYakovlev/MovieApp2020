package ru.spb.yakovlev.androidacademy2020.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.spb.yakovlev.androidacademy2020.data.GetMovieDetailsState
import ru.spb.yakovlev.androidacademy2020.data.MovieListMokkRepo
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieDetailsData

class MovieDetailsViewModel : ViewModel() {

    private var movieId = 0
    lateinit var getMovieDetailsState: GetMovieDetailsState

    val movieDetailsState: StateFlow<DataState<MovieDetailsData>>
        get() {
            return when (movieId) {
                0 -> MutableStateFlow(DataState.Empty)
                else -> getMovieDetailsState.state
            }
        }

    fun setMovieId(id: Int) {
        movieId = id
        getMovieDetailsState = GetMovieDetailsState(movieId)

        viewModelScope.launch {
            getMovieDetailsState.update()
        }
    }

    fun handleLike(movieId: Int, isLike: Boolean) {
        MovieListMokkRepo.updateMovieLike(movieId, isLike)

    }
}