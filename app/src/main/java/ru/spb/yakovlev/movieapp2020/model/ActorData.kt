package ru.spb.yakovlev.movieapp2020.model

data class ActorData(
    val id: Int = 0,
    val name: String = "",
    val photo: String = "",
)

fun ActorData.toActorItemData() = ActorItemData(
    id = id,
    name = name,
    photo = photo,
)