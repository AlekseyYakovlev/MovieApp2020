package ru.spb.yakovlev.movieapp2020.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.spb.yakovlev.movieapp2020.data.db.AppDb
import ru.spb.yakovlev.movieapp2020.data.db.MovieDataDao
import ru.spb.yakovlev.movieapp2020.data.db.RemoteKeysDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDb(
        @ApplicationContext context: Context
    ): AppDb =
        Room
            .databaseBuilder(
                context,
                AppDb::class.java,
                AppDb.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesRemoteKeysDao(
        db: AppDb
    ): RemoteKeysDao =
        db.remoteKeysDao()

    @Provides
    @Singleton
    fun providesMovieDataDao(
        db: AppDb
    ): MovieDataDao =
        db.movieDataDao()
}