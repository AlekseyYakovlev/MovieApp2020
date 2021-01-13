package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.GenresResponse
import ru.spb.yakovlev.movieapp2020.model.Genre
import ru.spb.yakovlev.movieapp2020.model.Locale
import javax.inject.Inject

interface IGenresRepo {
    suspend fun getMovieGenres(): List<Genre>
}

class GenresRepo @Inject constructor(
    private val network: RestService,
    locale: Locale,
) : IGenresRepo {
    private val lang = locale.name

    private var movieGenresList: List<Genre>? = null

    override suspend fun getMovieGenres(): List<Genre> {
        if (movieGenresList == null) {
            movieGenresList = network.getMovieGenres(
                language = lang,
            ).genres.map { it.toGenre() }
        }
        return movieGenresList!!
    }

    private fun GenresResponse.GenreResp.toGenre() =
        Genre(
            id = id,
            name = name
        )
}