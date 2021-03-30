package ru.spb.yakovlev.movieapp2020.ui.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.spb.yakovlev.movieapp2020.model.ActorItemData
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.interactors.GetCertificationStateFlowUseCase
import ru.spb.yakovlev.movieapp2020.model.interactors.GetMovieDetailsCast
import ru.spb.yakovlev.movieapp2020.model.interactors.GetMovieDetailsStateUseCase
import ru.spb.yakovlev.movieapp2020.model.interactors.UpdateFavoritesUseCase
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsStateUseCase: GetMovieDetailsStateUseCase,
    private val getMovieDetailsCast: GetMovieDetailsCast,
    private val updateFavoritesUseCase: UpdateFavoritesUseCase,
    private val getCertificationStateFlowUseCase: GetCertificationStateFlowUseCase,
) : ViewModel() {

    private var movieDetailsScreenState = MovieDetailsScreenState()

    suspend fun showMovieDetails(
        movieId: Int,
        language: String,
        country: String,
    ): Flow<MovieDetailsScreenState> =
        combine(
            getMovieDetailsStateUseCase(movieId, language),
            getCertificationStateFlowUseCase(movieId, country),
            getMovieDetailsCast(movieId, language)
        ) { movieDetails, cert, cast ->

            when (movieDetails) {
                is DataState.Success -> movieDetailsScreenState = movieDetailsScreenState.copy(
                    id = movieDetails.data.id,
                    title = movieDetails.data.title,
                    poster = movieDetails.data.backdrop,
                    isLike = movieDetails.data.isLiked,
                    genre = movieDetails.data.genre,
                    voteAverage = movieDetails.data.voteAverage,
                    numberOfRatings = movieDetails.data.numberOfRatings,
                    overview = movieDetails.data.overview,
                )
                DataState.Empty -> {}
                is DataState.Error -> {}
                is DataState.Loading -> {}
            }

            when (cert) {
                is DataState.Success -> movieDetailsScreenState = movieDetailsScreenState.copy(
                    certification = cert.data
                )
                else -> {
                }
            }

            when (cast) {
                is DataState.Success -> movieDetailsScreenState = movieDetailsScreenState.copy(
                    actorItemsData = cast.data
                )
                else -> {
                }
            }

            movieDetailsScreenState
        }

    fun handleLike(movieId: Int, isLike: Boolean) {
        viewModelScope.launch {
            updateFavoritesUseCase(movieId, isLike)
        }
    }
}

data class MovieDetailsScreenState(
    val id: Int = 0,
    val title: String = "",
    val poster: String = "",
    val certification: String = "",
    val isLike: Boolean = false,
    val genre: String = "",
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val overview: String = "",
    val actorItemsData: List<ActorItemData> = listOf(),
    )