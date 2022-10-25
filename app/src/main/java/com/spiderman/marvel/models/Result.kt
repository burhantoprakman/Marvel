package com.spiderman.marvel.models

import java.io.Serializable

data class Result (
    val id : Int,
    val comics: Comics,
    val description: String,
    val events: Events,
    val name: String,
    val series: Series,
    val stories: Stories,
    val modified: String?,
    val creators : Creators?,
    val thumbnail: Thumbnail?,
    val urls: List<Url>,
    var listType : String?
) : Serializable