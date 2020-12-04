package ru.spb.yakovlev.androidacademy2020.model

import java.util.*

data class ActorData(
    val id: Int = 0,
    val name: String = "",
    val photo: String = "",
    val bornDate: Date? = null,
    val bornPlace: String? = null,
    val diedDate: Date? = null,
    val diedPlace: String? = null,
    val tag: String = "",
    val biography: String = "",
    val filmIds: List<Int> = listOf(),
)

fun ActorData.toActorItemData() = ActorItemData(
    id = id,
    name = name,
    photo = photo,
)