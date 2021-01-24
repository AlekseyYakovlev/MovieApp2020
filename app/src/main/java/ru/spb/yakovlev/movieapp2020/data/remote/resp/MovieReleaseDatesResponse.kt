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
        val country: String = "",
        @SerialName("release_dates")
        val releaseDates: List<ReleaseDate> = emptyList()
    ) {
        @Serializable
        data class ReleaseDate(
            @SerialName("certification")
            val certification: String = "",
        )
    }
}