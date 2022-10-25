package com.spiderman.marvel.models

data class Stories(
    val available: Int,
    val collectionURI: String,
    val returned: Int,
    val items: List<Item>,
    var expandable:Boolean = false
)