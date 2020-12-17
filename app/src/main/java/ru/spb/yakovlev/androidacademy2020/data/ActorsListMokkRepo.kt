package ru.spb.yakovlev.androidacademy2020.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.spb.yakovlev.androidacademy2020.App
import ru.spb.yakovlev.androidacademy2020.model.ActorItemData

object ActorsListMokkRepo {

    fun getActorItemsById(ids: List<Int>) = loadActors().filter { it.id in ids }

    private val jsonFormat = Json { ignoreUnknownKeys = true }

    private fun loadActors(): List<ActorItemData> =
        run {
            val data = readAssetFileToString("people.json")
            parseActors(data)
        }

    private fun readAssetFileToString(fileName: String): String {
        val stream = App.INSTANCE.assets.open(fileName)
        return stream.bufferedReader().readText()
    }

    private fun parseActors(data: String): List<ActorItemData> {
        val jsonActors = jsonFormat.decodeFromString<List<JsonActor>>(data)
        return jsonActors.map {
            ActorItemData(
                id = it.id,
                name = it.name,
                photo = it.profilePicture
            )
        }
    }

    @Serializable
    private class JsonActor(
        val id: Int,
        val name: String,
        @SerialName("profile_path")
        val profilePicture: String
    )
}