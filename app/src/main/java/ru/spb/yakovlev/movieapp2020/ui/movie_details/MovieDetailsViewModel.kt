package ru.spb.yakovlev.movieapp2020.ui.movie_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.MovieDetailsDataWithCast
import ru.spb.yakovlev.movieapp2020.model.interactors.GetCertificationUseCase
import ru.spb.yakovlev.movieapp2020.model.interactors.GetMovieDetailsStateUseCase
import ru.spb.yakovlev.movieapp2020.model.interactors.UpdateFavoritesUseCase

class MovieDetailsViewModel @ViewModelInject constructor(
    private val getMovieDetailsStateUseCase: GetMovieDetailsStateUseCase,
    private val updateFavoritesUseCase: UpdateFavoritesUseCase,
    private val getCertificationUseCase: GetCertificationUseCase,
) : ViewModel() {

    suspend fun showMovieDetails(
        movieId: Int,
        language: String,
    ): Flow<DataState<MovieDetailsDataWithCast>> =
        getMovieDetailsStateUseCase(movieId, language)


    fun handleLike(movieId: Int, isLike: Boolean) {
        viewModelScope.launch {
            updateFavoritesUseCase(movieId, isLike)
        }
    }

    suspend fun getCertification(movieId: Int, country: String): String =
        withContext(Dispatchers.IO) { getCertificationUseCase(movieId, country) }
}