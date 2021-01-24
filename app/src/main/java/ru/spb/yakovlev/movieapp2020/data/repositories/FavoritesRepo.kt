package ru.spb.yakovlev.movieapp2020.data.repositories

import ru.spb.yakovlev.movieapp2020.data.db.daos.FavoritesDao
import ru.spb.yakovlev.movieapp2020.data.db.entities.FavoriteEntity
import javax.inject.Inject

class FavoritesRepo @Inject constructor(
    private val favoritesDao: FavoritesDao
) {
    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        if (isFavorite) favoritesDao.add(FavoriteEntity(movieId))
        else favoritesDao.remove(FavoriteEntity(movieId))
    }
}