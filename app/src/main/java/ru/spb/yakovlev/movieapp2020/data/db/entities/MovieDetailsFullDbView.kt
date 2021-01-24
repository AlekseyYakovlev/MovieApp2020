package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT md.id, md.title, md.genre, md.runtime, c.certification, md.voteAverage,
            md.numberOfRatings, md.backdrop, md.overview, (f.movieId IS NOT NULL) as isLiked     
        FROM movies_details AS md
        LEFT JOIN movies_certifications AS c on md.id = c.movieId
        LEFT JOIN favorites AS f on md.id = f.movieId
    """
)
class MovieDetailsFullDbView(
    val id: Int = 0,
    val title: String = "",
    val genre: String = "",
    val runtime: Int = 0,
    val certification: String? = "",
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val backdrop: String = "",
    val overview: String = "",
    val isLiked: Boolean = false
)