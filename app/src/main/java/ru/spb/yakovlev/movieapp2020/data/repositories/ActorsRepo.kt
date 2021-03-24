package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.room.withTransaction
import ru.spb.yakovlev.movieapp2020.data.db.AppDb
import ru.spb.yakovlev.movieapp2020.data.db.entities.ActorEntity
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieActorXRef
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.err.ApiError
import ru.spb.yakovlev.movieapp2020.data.remote.err.NoNetworkError
import ru.spb.yakovlev.movieapp2020.data.remote.resp.CreditsResponse
import ru.spb.yakovlev.movieapp2020.model.ActorData
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import ru.spb.yakovlev.movieapp2020.model.DataState
import javax.inject.Inject

class ActorsRepo @Inject constructor(
    private val network: RestService,
    private val apiSettings: ApiSettings,
    private val db: AppDb,
) {

    suspend fun getCastByMovieId(movieId: Int, language: String): DataState<List<ActorData>> {
        getFromDb(movieId).also {
            if (it.isNotEmpty()) {
                return DataState.Success(it)
            }
        }

        val castFromNet = try {
            getFromNet(movieId, language)
        } catch (e: ApiError) {
            return DataState.Error(e.message)
        } catch (e: NoNetworkError) {
            return DataState.Error(e.message)
        }

        saveToDb(castFromNet, movieId)

        getFromDb(movieId).also {
            return if (it.isNotEmpty()) {
                DataState.Success(it)
            } else DataState.Error("No data")
        }
    }


    private suspend fun getFromDb(movieId: Int): List<ActorData> =
        db.actorsDao().getActorsByMovieId(movieId).map { it.toActorData() }

    private suspend fun getFromNet(movieId: Int, language: String): List<CreditsResponse.Cast> {
        val actorsDataFromNet = network.getMovieCredits(
            movieId = movieId,
            language = language,
        )
        return actorsDataFromNet.cast
    }

    private suspend fun saveToDb(castFromNet: List<CreditsResponse.Cast>, movieId: Int) {
        val actors = castFromNet.map { it.toActorEntity() }
        val refs = castFromNet.map { it.toMovieActorXRef(movieId) }
        db.withTransaction {
            db.actorsDao().insertAll(actors)
            db.movieActorXRefsDao().insertAll(refs)
        }
    }

    private fun CreditsResponse.Cast.toActorEntity() =
        ActorEntity(
            id = id,
            name = name,
            photo = profilePath ?: ""
        )

    private fun CreditsResponse.Cast.toMovieActorXRef(movieId: Int) =
        MovieActorXRef(
            movieId = movieId,
            actorId = id,
            castId = castId,
        )

    private fun ActorEntity.toActorData() =
        ActorData(
            id = id,
            name = name,
            photo = if (photo.isNotBlank()) "${apiSettings.secureBaseUrl}${apiSettings.profileSize}$photo" else "",
        )
}