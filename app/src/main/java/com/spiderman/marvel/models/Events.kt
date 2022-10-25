package com.spiderman.marvel.models

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int,
    var expandable:Boolean = false
)