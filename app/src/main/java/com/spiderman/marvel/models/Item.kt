package com.spiderman.marvel.models

data class Item(
    val name: String,
    val resourceURI: String,
    var expandable:Boolean = false
)