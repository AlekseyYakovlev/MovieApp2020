package ru.spb.yakovlev.movieapp2020.data.remote.resp


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReleaseDatesResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("results")
    val results: List<Result>
) {
    @Serializable
    data class Result(
        @SerialName("iso_3166_1")
        val iso31661: String,
        @SerialName("release_dates")
        val releaseDates: List<ReleaseDate>
    ) {
        @Serializable
        data class ReleaseDate(
            @SerialName("certification")
            val certification: String,
            @SerialName("iso_639_1")
            val iso6391: String,
            @SerialName("note")
            val note: String,
            @SerialName("release_date")
            val releaseDate: String,
            @SerialName("type")
            val type: Int
        )
    }
}