package ru.spb.yakovlev.movieapp2020.data.db.entities

import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT m.uid, m.id, m.title, GROUP_CONCAT (g.name, ", ") AS genre,
            md.runtime, c.certification, m.voteAverage, m.numberOfRatings, 
            m.poster, (f.movieId IS NOT NULL) as isLiked     
        FROM movies AS m
        LEFT JOIN movie_genre_x_refs AS mg_ref on m.id = mg_ref.movie_id
        LEFT JOIN genres AS g on mg_ref.genre_id = g.id
        LEFT JOIN movies_certifications AS c on m.id = c.movieId
        LEFT JOIN movies_details AS md on m.id = md.id
        LEFT JOIN favorites AS f on m.id = f.movieId
        GROUP BY m.id
        ORDER BY m.uid
    """
)
class PopularMovieEntity(
    val uid: Int = 0,
    val id: Int = 0,
    val title: String = "",
    val genre: String? = "",
    val runtime: Int = 0,
    val certification: String? = "",
    val voteAverage: Float = 0f,
    val numberOfRatings: Int = 0,
    val poster: String = "",
    val isLiked: Boolean = false
)