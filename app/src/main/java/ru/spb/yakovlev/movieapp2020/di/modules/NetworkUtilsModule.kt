package ru.spb.yakovlev.movieapp2020.di.modules

import android.content.Context
import coil.ImageLoader
import coil.util.CoilUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.spb.yakovlev.movieapp2020.data.remote.NetworkMonitor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkUtilsModule {
    @Provides
    @Singleton
    fun providesNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor =
        NetworkMonitor(context)

    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader =
        ImageLoader.Builder(context)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(context))
                    .build()
            }
            .crossfade(true)
            .build()
}