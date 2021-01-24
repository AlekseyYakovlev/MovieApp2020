package ru.spb.yakovlev.movieapp2020.model

import ru.spb.yakovlev.movieapp2020.ui.base.RvItemData

data class MovieItemData(
    val uid: Int = 0,
    override val id: Int = 0,
    val title: String = "",
    val genre: String = "",
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val poster: String = "",
    val isLike: Boolean = false
) : RvItemData