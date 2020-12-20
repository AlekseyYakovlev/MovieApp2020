package ru.spb.yakovlev.movieapp2020.model

import ru.spb.yakovlev.movieapp2020.ui.base.RvItemData

data class MovieItemData(
    override val id: Int = 0,
    val title: String = "",
    val genre: String = "",
    val runtime: Int = 0,
    val minimumAge: String = "6+",
    val ratings: Float = 0f,
    val numberOfRatings: Int = 0,
    val poster: String = "",
    val isLike: Boolean = false
) : RvItemData