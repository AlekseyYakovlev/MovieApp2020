package ru.spb.yakovlev.movieapp2020.ui.movies_list

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import ru.spb.yakovlev.movieapp2020.model.interactors.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getMoviesListPopularUseCase: GetMoviesListPopularUseCase,
    private val updateFavoritesUseCase: UpdateFavoritesUseCase,
    private val getCertificationUseCase: GetCertificationUseCase,
    private val getMovieRuntimeUseCase: GetMovieRuntimeUseCase,
    private val loadGenresStateUseCase: LoadGenresStateUseCase,
) : ViewModel() {
    private var popularMoviesStream: Flow<PagingData<MovieItemData>>? = null

    private val job = SupervisorJob()
    private val handler = CoroutineExceptionHandler { _, exception ->
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

        CoroutineScope(
            scope.launch { loadGenresStateUseCase(language) }
        )

        val newResult: Flow<PagingData<MovieItemData>> =
            getMoviesListPopularUseCase(language, country)
                .flowOn(Dispatchers.IO)
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