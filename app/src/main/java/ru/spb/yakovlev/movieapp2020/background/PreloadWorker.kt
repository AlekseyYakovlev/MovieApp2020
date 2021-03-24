package ru.spb.yakovlev.movieapp2020.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.model.interactors.PreloadImages
import ru.spb.yakovlev.movieapp2020.model.interactors.PreloadMoviesCertification
import ru.spb.yakovlev.movieapp2020.model.interactors.PreloadMoviesDetails
import ru.spb.yakovlev.movieapp2020.model.interactors.PreloadMoviesListPopular

@HiltWorker
class PreloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    private val preloadMoviesListPopular: PreloadMoviesListPopular,
    private val preloadMoviesDetails: PreloadMoviesDetails,
    private val preloadMoviesCertification: PreloadMoviesCertification,
    private val preloadImages: PreloadImages,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val lang = context.resources.getString(R.string.app_locale)
        val country = context.resources.getString(R.string.app_default_location)

        try {
//            preloadMoviesListPopular(lang, country)
//            preloadMoviesCertification(country)
//            preloadMoviesDetails(lang)
            preloadImages(context, lang)
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}
