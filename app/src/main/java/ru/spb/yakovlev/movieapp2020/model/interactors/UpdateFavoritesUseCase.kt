package ru.spb.yakovlev.movieapp2020.model.interactors

import ru.spb.yakovlev.movieapp2020.data.repositories.FavoritesRepo
import javax.inject.Inject

class UpdateFavoritesUseCase @Inject constructor(
    private val favoritesRepo: FavoritesRepo
) : IUseCase {
    suspend operator fun invoke(movieId: Int, isFavorite: Boolean) =
        favoritesRepo.updateFavoriteStatus(movieId, isFavorite)
}