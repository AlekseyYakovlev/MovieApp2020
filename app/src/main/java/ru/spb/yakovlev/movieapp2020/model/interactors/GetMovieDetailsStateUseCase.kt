package ru.spb.yakovlev.movieapp2020.model.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import ru.spb.yakovlev.movieapp2020.data.repositories.MovieDetailsRepo
import ru.spb.yakovlev.movieapp2020.data.repositories.ActorsRepo
import ru.spb.yakovlev.movieapp2020.model.*
import javax.inject.Inject

class GetMovieDetailsStateUseCase @Inject constructor(
    private val movieDetailsRepo: MovieDetailsRepo,
    private val actorsRepo: ActorsRepo,
    ) : IUseCase {

    suspend operator fun invoke(
        movieId: Int,
        language: String,
    ): Flow<DataState<MovieDetailsDataWithCast>> {

        val actorsFlow = flow<List<ActorItemData>?> {
            emit(null)
            emit(emptyList())
            emit(null)
            val actors = actorsRepo.getCastByMovieId(movieId, language)
            emit(actors.map { actorData -> actorData.toActorItemData() })
        }

        val moveDetailsFlow = movieDetailsRepo.getMoveDetailsById(movieId, language)

        return moveDetailsFlow.combine(actorsFlow) { md, actors ->
            actors?.let { DataState.Success(md.toMovieDetailsDataWithCast(actors)) }
                ?: DataState.Loading(null)
        }
    }

    private fun MovieDetailsData.toMovieDetailsDataWithCast(actors: List<ActorItemData>) =
        MovieDetailsDataWithCast(
            id = id,
            title = title,
            genre = genre,
            runtime = runtime,
            minimumAge = certification,
            voteAverage = voteAverage,
            numberOfRatings = numberOfRatings,
            backdrop = backdrop,
            isLike = isLiked,
            overview = overview,
            actorItemsData = actors,
        )
}