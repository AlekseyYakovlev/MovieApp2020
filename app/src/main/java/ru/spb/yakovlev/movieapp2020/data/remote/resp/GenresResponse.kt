package ru.spb.yakovlev.movieapp2020.data.remote.resp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    @SerialName("genres")
    val genres: List<GenreResp>
) {
    @Serializable
    data class GenreResp(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String
    )
}