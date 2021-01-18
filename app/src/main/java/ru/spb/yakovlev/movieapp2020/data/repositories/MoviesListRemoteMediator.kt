package ru.spb.yakovlev.movieapp2020.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.spb.yakovlev.movieapp2020.data.db.AppDb
import ru.spb.yakovlev.movieapp2020.data.db.MovieDataDao
import ru.spb.yakovlev.movieapp2020.data.db.RemoteKeysDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.RemoteKeys
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.MovieResponse
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import ru.spb.yakovlev.movieapp2020.model.Locale
import ru.spb.yakovlev.movieapp2020.model.MovieData
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import javax.inject.Inject
import kotlin.math.roundToInt

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MoviesListRemoteMediator @Inject constructor(
    private val network: RestService,
    private val apiSettings: ApiSettings,
    private val movieDataDao: MovieDataDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val db: AppDb,
    locale: Locale,
) : RemoteMediator<Int, MovieData>() {

    private val lang = locale.name
    private val region = locale.country

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieData>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Timber.tag("123456").d("LoadType.REFRESH ")
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Timber.tag("123456").d("LoadType.PREPEND")
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                // If the previous key is null, then we can't request more data
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                Timber.tag("123456").d("LoadType.APPEND")
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                Timber.tag("123456").d("APPEND nextKey = ${remoteKeys.nextKey}")
                remoteKeys.nextKey
            }
        }
        //val apiQuery = query + IN_QUALIFIER

        try {
            //val apiResponse = service.searchRepos(apiQuery, page, state.config.pageSize)
            val response = network.getPopular(
                page = page,
                language = lang,
                region = region
            )
            Timber.tag("123456").d("try response  page = $page  response.page ${response.page}")
            val moviesList = response.movies.map { it.toMovieData() }
            val endOfPaginationReached = (response.page >= response.totalPages)
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    movieDataDao.clearRepos()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else (page - 1)
                val nextKey = if (endOfPaginationReached) null else (page + 1)
                Timber.tag("123456").d("page = $page prevKey = $prevKey nextKey = $nextKey")
                val keys = moviesList.map {
                    RemoteKeys(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insertAll(keys)
                movieDataDao.insertAll(moviesList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieData>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                Timber.tag("123456").d("movie.id = ${movie.id} ${movie.title}")
                Timber.tag("123456").d("state.anchorPosition = ${state.anchorPosition}")
                // Get the remote keys of the last item retrieved
                remoteKeysDao.remoteKeysByMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieData>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                remoteKeysDao.remoteKeysByMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieData>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                remoteKeysDao.remoteKeysByMovieId(movieId)
            }
        }
    }

    private fun MovieResponse.toMovieData() =
        MovieData(
            id = id,
            title = title,
            genres = genreIds,
            runtime = 0,
            minimumAge = if (isAdult) "18+" else "13+",
            voteAverage = voteAverage.roundRating(),
            numberOfRatings = voteCount,
            poster = "${apiSettings.secureBaseUrl}${apiSettings.posterSize}$posterPath",
            backdrop = backdropPath,
            isLike = false,
            overview = overview,
        )

    private fun Double.roundRating() = this.roundToInt().toFloat() / 2
}