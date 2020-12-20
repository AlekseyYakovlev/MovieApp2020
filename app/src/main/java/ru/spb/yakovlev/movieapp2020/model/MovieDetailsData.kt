package ru.spb.yakovlev.movieapp2020.model

data class MovieDetailsData (
    val id: Int = 0,
    val title: String = "",
    val genre: String = "",
    val runtime: Int = 0,
    val minimumAge: String = "6+",
    val ratings: Float = 0f,
    val numberOfRatings: Int = 0,
    val backdrop: String = "",
    val isLike: Boolean = false,
    val overview: String = "",
    val actorItemsData: List<ActorItemData> = listOf(),
)