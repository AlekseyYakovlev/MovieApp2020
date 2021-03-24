package ru.spb.yakovlev.movieapp2020.background

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder

class WorkRepository {
    private val constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    val workRequest =
        OneTimeWorkRequestBuilder<PreloadWorker>()
//        PeriodicWorkRequestBuilder<PreloadWorker>(
//            8, TimeUnit.HOURS,
//            15, TimeUnit.MINUTES
//        )
            .setConstraints(constraints)
            .build()


}