package ru.spb.yakovlev.movieapp2020.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.spb.yakovlev.movieapp2020.BuildConfig
import ru.spb.yakovlev.movieapp2020.data.db.entities.RemoteKeys
import ru.spb.yakovlev.movieapp2020.model.Genre
import ru.spb.yakovlev.movieapp2020.model.MovieData

@Database(
    entities = [
        MovieData::class,
        RemoteKeys::class,
        Genre::class,
    ],
    version = AppDb.DATABASE_VERSION,
    exportSchema = false,
    //views = []
)
@TypeConverters(ListConverter::class)
abstract class AppDb : RoomDatabase() {

    abstract fun movieDataDao(): MovieDataDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db"
        const val DATABASE_VERSION = 1
    }
}