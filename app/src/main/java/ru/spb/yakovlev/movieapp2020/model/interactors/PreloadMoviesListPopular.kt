package ru.spb.yakovlev.movieapp2020.model.interactors

import ru.spb.yakovlev.movieapp2020.data.repositories.PopularMovieListRepo
import javax.inject.Inject

class PreloadMoviesListPopular @Inject constructor(
    private val popularMovieListRepo: PopularMovieListRepo
) : IUseCase {

    suspend operator fun invoke(
        lang: String,
        country: String,
        cacheSize: Int = DEFAULT_CACHE_SIZE
    ) {
        popularMovieListRepo.loadPopularMoviesFromNetAndSaveToDb(cacheSize, lang, country)
    }
}

private const val DEFAULT_CACHE_SIZE = 20 // in pages