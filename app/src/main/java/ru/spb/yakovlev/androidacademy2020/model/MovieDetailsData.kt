package ru.spb.yakovlev.androidacademy2020.model

data class MovieDetailsData (
    val id: Int = 0,
    val title: String = "",
    val tags: String = "",
    val duration: Int = 0,
    val pg: String = "6+",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val poster2: String = "",
    val isLike: Boolean = false,
    val storyLine: String = "",
    val actorItemsData: List<ActorItemData> = listOf(),
)