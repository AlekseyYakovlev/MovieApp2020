package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.db.daos.GenresDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.GenreEntity
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.GenresResponse
import ru.spb.yakovlev.movieapp2020.model.Genre
import javax.inject.Inject

class GenresRepo @Inject constructor(
    private val network: RestService,
    private val genresDao: GenresDao,
) {
    private var movieGenresList: List<Genre>? = null
    private var currentLang: String = ""

    suspend fun loadGenres(lang: String): List<Genre> {
        if (currentLang.isEmpty()) currentLang = genresDao.language()
        if (movieGenresList == null || currentLang != lang) {
            val movieGenresEntitiesList = network.getMovieGenres(
                language = lang,
            ).genres.map { it.toGenreEntity(lang) }

            currentLang = lang
            movieGenresEntitiesList.let { genresDao.replaceAll(it) }
            movieGenresList = movieGenresEntitiesList.map { it.toGenre() }
        }
        return movieGenresList ?: emptyList()
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