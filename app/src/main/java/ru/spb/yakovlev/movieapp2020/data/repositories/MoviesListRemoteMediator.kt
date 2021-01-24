package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.spb.yakovlev.movieapp2020.data.db.AppDb
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieDataEntity
import ru.spb.yakovlev.movieapp2020.data.db.entities.MovieGenreXRef
import ru.spb.yakovlev.movieapp2020.data.db.entities.PopularMovieDbView
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MovieResponse
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MoviesListRemoteMediator @Inject constructor(
    private val network: RestService,
    private val apiSettings: ApiSettings,
    private val db: AppDb,
) : RemoteMediator<Int, PopularMovieDbView>() {
    init {
        Timber.tag("123456789").d("MediatorResult init")
    }

    lateinit var lang: String
    lateinit var country: String

    private var currentPage = STARTING_PAGE_INDEX

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovieDbView>
    ): MediatorResult {
        Timber.tag("123456789").d("MediatorResult load lang = $lang")
        val page = when (loadType) {
            LoadType.REFRESH -> {
                Timber.tag("12345").d("LoadType.REFRESH")
                STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Timber.tag("12345").d("LoadType.PREPEND")
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                Timber.tag("12345").d("LoadType.APPEND ${currentPage + 1}")
                (currentPage + 1)
            }
        }

        try {
            currentPage = page
            val response = network.getPopular(
                page = page,
                language = lang,
                region = country
            )
            val moviesList = response.movies.map { it.toMovieData() }
            val xRefs = response.movies.flatMap { movie ->
                movie.genreIds.map { genreId -> MovieGenreXRef(movie.id, genreId) }
            }
            val endOfPaginationReached = (response.page >= response.totalPages)
            db.withTransaction {
                // clear tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.movieDataDao().clear()
                }

                db.movieDataDao().insertAll(moviesList)
                db.movieGenreXRefDao().insertAll(xRefs)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            Timber.e("MediatorResult IOException ${exception.message}")
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            Timber.e("MediatorResult HttpException ${exception.message}")
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            Timber.e("MediatorResult Exception ${exception.message}")
            return MediatorResult.Error(exception)
        }
    }

    private fun MovieResponse.toMovieData() =
        MovieDataEntity(
            id = id,
            title = title,
            voteAverage = voteAverage.roundRating(),
            numberOfRatings = voteCount,
            poster = "${apiSettings.secureBaseUrl}${apiSettings.posterSize}$posterPath",
            language = lang
        )

    private fun Double.roundRating() = this.roundToInt().toFloat() / 2
}