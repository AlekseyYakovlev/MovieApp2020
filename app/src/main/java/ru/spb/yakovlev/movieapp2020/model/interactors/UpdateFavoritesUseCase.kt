package ru.spb.yakovlev.movieapp2020.model.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.spb.yakovlev.movieapp2020.data.repositories.FavoritesRepo
import javax.inject.Inject

class UpdateFavoritesUseCase @Inject constructor(
    private val favoritesRepo: FavoritesRepo
): IUseCase {
    suspend operator fun invoke(movieId: Int, isFavorite: Boolean) = withContext(Dispatchers.IO){
        favoritesRepo.updateFavoriteStatus(movieId, isFavorite)
    }
}