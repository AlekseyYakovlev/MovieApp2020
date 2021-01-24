package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.room.withTransaction
import ru.spb.yakovlev.movieapp2020.data.db.AppDb
import ru.spb.yakovlev.movieapp2020.data.db.entities.ActorEntity
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieActorXRef
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.CreditsResponse
import ru.spb.yakovlev.movieapp2020.model.ActorData
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import javax.inject.Inject

class ActorsRepo @Inject constructor(
    private val network: RestService,
    private val apiSettings: ApiSettings,
    private val db: AppDb,
) {

    suspend fun getCastByMovieId(movieId: Int, language: String): List<ActorData> {
        if (db.movieActorXRefsDao().getFirstUidForMovieId(movieId) == null) {
            val actorsDataFromNet = network.getMovieCredits(
                movieId = movieId,
                language = language,
            ).cast
            val actors = actorsDataFromNet.map { it.toActorEntity() }
            val refs = actorsDataFromNet.map { it.toMovieActorXRef(movieId) }

            db.withTransaction {
                db.actorsDao().insertAll(actors)
                db.movieActorXRefsDao().insertAll(refs)
            }
        }
        return db.actorsDao().getActorsByMovieId(movieId).map { it.toActorData() }
    }

    private fun CreditsResponse.Cast.toActorEntity() =
        ActorEntity(
            id = castId,
            name = name,
            photo = profilePath ?: ""
        )

    private fun CreditsResponse.Cast.toMovieActorXRef(movieId: Int) =
        MovieActorXRef(
            movieId = movieId,
            actorId = castId,
        )

    private fun ActorEntity.toActorData() =
        ActorData(
            id = id,
            name = name,
            photo = if (photo.isNotBlank()) "${apiSettings.secureBaseUrl}${apiSettings.profileSize}$photo" else "",
        )
}