package ru.spb.yakovlev.androidacademy2020.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.spb.yakovlev.androidacademy2020.data.MovieListDummyRepo
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieItemData

class MoviesListViewModel : ViewModel() {
    private val _moviesListState = MutableStateFlow<DataState<List<MovieItemData>>>(DataState.Empty)
    val moviesListState: StateFlow<DataState<List<MovieItemData>>> = _moviesListState

    init {
        generateDataFlow()
    }

    fun handleLike(movieId: Int, isLike: Boolean) {
        MovieListDummyRepo.updateMovieLike(movieId, isLike)
        update()
    }

    fun update() {
        _moviesListState.value = DataState.Success(MovieListDummyRepo.getMovieItemList())
    }

    /**
     * Data loading process emulator
     */
    private fun generateDataFlow() {
        viewModelScope.launch {
            (0..30).forEach {
                _moviesListState.value = DataState.Loading(it)
                delay(10L)
            }
            _moviesListState.value = DataState.Error("Loading error, restarting loading...")
            delay(500L)
            (0..33).forEach {
                _moviesListState.value = DataState.Loading(it)
                delay(30L)
            }
            delay(500L)
            _moviesListState.value = DataState.Success(MovieListDummyRepo.getMovieItemList().take(4))
            (34..66).forEach {
                _moviesListState.value = DataState.Loading(it)
                delay(30L)
            }
            delay(500L)
            _moviesListState.value = DataState.Success(MovieListDummyRepo.getMovieItemList().take(8))
            (67..100).forEach {
                _moviesListState.value = DataState.Loading(it)
                delay(30L)
            }
            delay(500L)
            _moviesListState.value = DataState.Success(MovieListDummyRepo.getMovieItemList())
        }
    }

}