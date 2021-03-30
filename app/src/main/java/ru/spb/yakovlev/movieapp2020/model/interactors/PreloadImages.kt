package ru.spb.yakovlev.movieapp2020.model.interactors

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import ru.spb.yakovlev.movieapp2020.data.repositories.MovieCertificationsRepo
import ru.spb.yakovlev.movieapp2020.data.repositories.PopularMovieListRepo
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import timber.log.Timber
import javax.inject.Inject

class PreloadImages @Inject constructor(
    private val popularMovieListRepo: PopularMovieListRepo,
    private val imageLoader: ImageLoader,
    private val apiSettings: ApiSettings,
) : IUseCase {
    suspend operator fun invoke(context: Context, lang: String) {

        popularMovieListRepo.getPosterPaths(lang).forEach {
            Timber.d(it)
            val request = ImageRequest.Builder(context)
                .data("${apiSettings.secureBaseUrl}${apiSettings.posterSize}${it}")
                // Optional, but setting a ViewSizeResolver will conserve memory by limiting the size the image should be preloaded into memory at.
                //.size(ViewSizeResolver(imageView))
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
            imageLoader.enqueue(request)

        }
    }
}