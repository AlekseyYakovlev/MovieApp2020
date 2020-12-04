package ru.spb.yakovlev.androidacademy2020.model

import ru.spb.yakovlev.androidacademy2020.data.ActorsListDummyRepo

data class MovieData(
    val id: Int = 0,
    val title: String = "",
    val tags: String = "",
    val duration: Int = 0,
    val pg: String = "6+",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val poster: String = "",
    val poster2: String = "",
    val isLike: Boolean = false,
    val storyLine: String = "",
    val castIds: List<Int> = listOf(),
)

fun MovieData.toMovieItemData() = MovieItemData(
    id = id,
    title = title,
    tags = tags,
    duration = duration,
    pg = pg,
    rating = rating,
    reviewsCount = reviewsCount,
    poster = poster,
    isLike = isLike,
)

fun MovieData.toMovieMovieDetailsData() = MovieDetailsData(
    id = id,
    title = title,
    tags = tags,
    duration = duration,
    pg = pg,
    rating = rating,
    reviewsCount = reviewsCount,
    poster2 = poster2,
    isLike = isLike,
    storyLine = storyLine,
    actorItemsData = ActorsListDummyRepo.getActorItemsById(castIds),
)