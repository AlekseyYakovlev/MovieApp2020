package ru.spb.yakovlev.movieapp2020.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.spb.yakovlev.movieapp2020.data.db.*
import ru.spb.yakovlev.movieapp2020.data.db.daos.*
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
    fun providesMovieDataDao(
        db: AppDb
    ): MovieDataDao =
        db.movieDataDao()

    @Provides
    @Singleton
    fun providesGenresDao(
        db: AppDb
    ): GenresDao =
        db.genresDao()

    @Provides
    @Singleton
    fun providesMovieGenreXRefDao(
        db: AppDb
    ): MovieGenreXRefsDao =
        db.movieGenreXRefDao()

    @Provides
    @Singleton
    fun providesPopularMoviesDao(
        db: AppDb
    ): PopularMoviesDao =
        db.popularMoviesDao()

    @Provides
    @Singleton
    fun providesFavoritesDao(
        db: AppDb
    ): FavoritesDao =
        db.favoritesDao()

    @Provides
    @Singleton
    fun providesMovieDetailsDao(
        db: AppDb
    ): MovieDetailsDao =
        db.movieDetailsDao()

    @Provides
    @Singleton
    fun providesMovieCertificationsDao(
        db: AppDb
    ): MovieCertificationsDao =
        db.movieCertificationsDao()

    @Provides
    @Singleton
    fun providesMovieDetailsFullDao(
        db: AppDb
    ): MovieDetailsFullDao =
        db.movieDetailsFullDao()
}