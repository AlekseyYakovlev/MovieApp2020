package ru.spb.yakovlev.movieapp2020.data.remote.resp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListResponse(
    @SerialName("dates")
    val dates: Dates? = null,
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val movies: List<MovieResp>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
) {
    @Serializable
    data class Dates(
        @SerialName("maximum")
        val maximum: String,
        @SerialName("minimum")
        val minimum: String
    )
}