package ru.spb.yakovlev.androidacademy2020.model

data class MovieItemData(
    val id: Int = 0,
    val name: String = "",
    val tags: String = "",
    val duration: Int = 0,
    val pg: String = "6+",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val poster: String = "",
    val isLike: Boolean = false
)