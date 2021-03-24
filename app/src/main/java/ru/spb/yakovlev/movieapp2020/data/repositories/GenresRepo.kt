package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.db.daos.GenresDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.GenreEntity
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.err.ApiError
import ru.spb.yakovlev.movieapp2020.data.remote.err.NoNetworkError
import ru.spb.yakovlev.movieapp2020.data.remote.resp.GenresResponse
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.Genre
import javax.inject.Inject

class GenresRepo @Inject constructor(
    private val network: RestService,
    private val genresDao: GenresDao,
) {
    private var currentLang: String = ""

    suspend fun getGenres(lang: String): DataState<List<Genre>> {
        if (currentLang.isEmpty()) currentLang = genresDao.language()
        if (currentLang != lang) {
            val movieGenresEntitiesListFromNet = try {
                network.getMovieGenres(
                    language = lang,
                )
            } catch (e: ApiError) {
                return DataState.Error(e.message)
            } catch (e: NoNetworkError) {
                return DataState.Error(e.message)
            }

            val movieGenresEntitiesList =
                movieGenresEntitiesListFromNet.genres.map { it.toGenreEntity(lang) }

            currentLang = lang
            movieGenresEntitiesList.let { genresDao.replaceAll(it) }
            return DataState.Success(movieGenresEntitiesList.map { it.toGenre() })
        } else {
            return DataState.Success(genresDao.getGenres().map { it.toGenre() })
        }
    }

    private fun GenresResponse.GenreResp.toGenreEntity(language: String) =
        GenreEntity(
            id = id,
            name = name,
            language = language,
        )

    private fun GenreEntity.toGenre() =
        Genre(
            id = id,
            name = name
        )
}