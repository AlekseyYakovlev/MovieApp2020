package ru.spb.yakovlev.movieapp2020.ui.movies_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import ru.spb.yakovlev.movieapp2020.model.interactors.GetCertificationUseCase
import ru.spb.yakovlev.movieapp2020.model.interactors.GetMovieRuntimeUseCase
import ru.spb.yakovlev.movieapp2020.model.interactors.GetMoviesListPopularUseCase
import ru.spb.yakovlev.movieapp2020.model.interactors.UpdateFavoritesUseCase
import timber.log.Timber

class MoviesListViewModel @ViewModelInject constructor(
    private val getMoviesListPopularUseCase: GetMoviesListPopularUseCase,
    private val updateFavoritesUseCase: UpdateFavoritesUseCase,
    private val getCertificationUseCase: GetCertificationUseCase,
    private val getMovieRuntimeUseCase: GetMovieRuntimeUseCase,
) : ViewModel() {
    private var popularMoviesStream: Flow<PagingData<MovieItemData>>? = null

    private val job = SupervisorJob()
    val handler = CoroutineExceptionHandler { _, exception ->
        Timber.tag("1234567 ExceptionH").e("exception $exception")
    }
    private val scope = CoroutineScope(job + Dispatchers.IO + handler)

    private var currentLanguage = ""
    private var currentCountry = ""


    suspend fun showPopularMovies(
        language: String,
        country: String
    ): Flow<PagingData<MovieItemData>> {
        val lastResult = popularMoviesStream

        if (lastResult != null && currentLanguage == language && currentCountry == country) {
            return lastResult
        }
        currentLanguage = language
        currentCountry = country

        val newResult: Flow<PagingData<MovieItemData>> =
            getMoviesListPopularUseCase(language, country)
                .cachedIn(scope)

        popularMoviesStream = newResult
        return newResult
    }

    suspend fun getRuntime(movieId: Int, language: String = currentLanguage): Int =
        withContext(Dispatchers.IO + handler) { getMovieRuntimeUseCase(movieId, language) }

    suspend fun getCertification(movieId: Int, country: String = currentCountry): String =
        withContext(Dispatchers.IO + handler) { getCertificationUseCase(movieId, country) }


    fun handleLike(movieId: Int, isLike: Boolean) {
        scope.launch {
            updateFavoritesUseCase.invoke(movieId, isLike)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }
}