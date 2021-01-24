package ru.spb.yakovlev.movieapp2020.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.spb.yakovlev.movieapp2020.BuildConfig
import ru.spb.yakovlev.movieapp2020.data.db.daos.*
import ru.spb.yakovlev.movieapp2020.data.db.entities.*

@Database(
    entities = [
        MovieDataEntity::class,
        GenreEntity::class,
        MovieGenreXRef::class,
        FavoriteEntity::class,
        MovieCertification::class,
        MovieDetailsEntity::class,
        MovieActorXRef::class,
        ActorEntity::class,
    ],
    version = AppDb.DATABASE_VERSION,
    exportSchema = false,
    views = [
        PopularMovieDbView::class,
        MovieDetailsFullDbView::class
    ]
)
@TypeConverters(ListConverter::class)
abstract class AppDb : RoomDatabase() {

    abstract fun movieDataDao(): MovieDataDao
    abstract fun genresDao(): GenresDao
    abstract fun movieGenreXRefDao(): MovieGenreXRefsDao
    abstract fun popularMoviesDao(): PopularMoviesDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun movieDetailsDao(): MovieDetailsDao
    abstract fun movieCertificationsDao(): MovieCertificationsDao
    abstract fun movieDetailsFullDao(): MovieDetailsFullDao
    abstract fun actorsDao(): ActorsDao
    abstract fun movieActorXRefsDao(): MovieActorXRefsDao

    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db"
        const val DATABASE_VERSION = 1
    }
}