package ru.spb.yakovlev.movieapp2020.di.modules

import androidx.work.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.spb.yakovlev.movieapp2020.background.PreloadWorker
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WorkManagerModule {

    @Provides
    @Singleton
    fun providesWorkRequest(constraints: Constraints): WorkRequest =
        PeriodicWorkRequestBuilder<PreloadWorker>(
            8, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

    @Provides
    @Singleton
    fun providesConstraints(): Constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
}