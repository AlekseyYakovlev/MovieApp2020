package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.resp.GenresResponse
import ru.spb.yakovlev.movieapp2020.model.ApiKey
import ru.spb.yakovlev.movieapp2020.model.Genre
import javax.inject.Inject

interface IGenresRepo {
    suspend fun getMovieGenres(language: String): List<Genre>
}

class GenresRepo @Inject constructor(
    private val network: RestService,
    apiKey: ApiKey,
) : IGenresRepo {
    private val key = apiKey.value

    private var movieGenresList: List<Genre>? = null

    override suspend fun getMovieGenres(language: String): List<Genre> {
        if (movieGenresList == null) {
            movieGenresList = network.getMovieGenres(
                apiKey = key,
                language = language,
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