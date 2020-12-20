package ru.spb.yakovlev.movieapp2020.model

import ru.spb.yakovlev.movieapp2020.ui.base.RvItemData

data class ActorItemData(
    override val id: Int = 0,
    val name: String = "",
    val photo: String = "",
) : RvItemData