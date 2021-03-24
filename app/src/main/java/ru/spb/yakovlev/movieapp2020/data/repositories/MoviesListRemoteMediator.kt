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
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MoviesListRemoteMediator @Inject constructor(
    private val network: RestService,
    private val db: AppDb,
) : RemoteMediator<Int, PopularMovieDbView>() {

    lateinit var lang: String
    lateinit var country: String
    lateinit var toMovieData: MovieResponse.(Int, String) -> MovieDataEntity

    private var currentPage = STARTING_PAGE_INDEX

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovieDbView>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                currentPage = db.movieDataDao().getLastPage(lang) ?: STARTING_PAGE_INDEX
                currentPage
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                (currentPage + 1)
            }
        }

        if (loadType == LoadType.REFRESH && currentPage != STARTING_PAGE_INDEX) {
            return MediatorResult.Success(endOfPaginationReached = false)
        }

        try {
            currentPage = page
            val response = network.getPopular(
                page = page,
                language = lang,
                region = country
            )
            val moviesList = response.movies.map { it.toMovieData(page, lang) }
            val xRefs = response.movies.flatMap { movie ->
                movie.genreIds.map { genreId -> MovieGenreXRef(movie.id, genreId) }
            }
            val endOfPaginationReached = (response.page >= response.totalPages)
            db.withTransaction {
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
}