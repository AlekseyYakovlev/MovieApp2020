package ru.spb.yakovlev.movieapp2020.ui.movies_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.yakovlev.movieapp2020.data.MovieListMokkRepo
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.MovieData
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import ru.spb.yakovlev.movieapp2020.model.toMovieItemData

class MoviesListViewModel : ViewModel() {
    val moviesListState: Flow<DataState<List<MovieItemData>>> =
        MovieListMokkRepo.moviesListState.map { it.toDataStateListMovieItemData() }

    init {
        update()
    }

    fun handleLike(movieId: Int, isLike: Boolean) {
        MovieListMokkRepo.updateMovieLike(movieId, isLike)
    }

    private fun update() {
        MovieListMokkRepo.loadMovies()
    }

    private fun DataState<List<MovieData>>.toDataStateListMovieItemData(): DataState<List<MovieItemData>> =
        when (this) {
            is DataState.Empty -> DataState.Empty
            is DataState.Loading -> DataState.Loading(progress)
            is DataState.Success -> DataState.Success(data.map { it.toMovieItemData() })
            is DataState.Error -> DataState.Error(errorMessage)
        }

}