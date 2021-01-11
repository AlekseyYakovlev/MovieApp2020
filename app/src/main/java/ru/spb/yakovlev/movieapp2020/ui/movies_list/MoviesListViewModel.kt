package ru.spb.yakovlev.movieapp2020.ui.movies_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import ru.spb.yakovlev.movieapp2020.model.interactors.GetMoviesListPopular

class MoviesListViewModel @ViewModelInject constructor(
    private val getMoviesListPopular: GetMoviesListPopular
) : ViewModel() {

    fun handleLike(movieId: Int, isLike: Boolean) {
        //TODO Fix "Like" action
    }

    suspend fun showPopularMovies(): Flow<PagingData<MovieItemData>> =
        getMoviesListPopular.getMoviesStream().cachedIn(viewModelScope)
}