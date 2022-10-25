package com.spiderman.marvel.repository

import com.spiderman.marvel.api.RetrofitInstance
import com.spiderman.marvel.util.Constants.Companion.DEFAULT_REQUEST_LIST_LIMIT

class MarvelHeroRepository {

    //Get main page marvel hero list
    suspend fun getMarvelHeroList(limit: Int, offset:Int ,ts: Long, apiKey: String, hash: String) =
        RetrofitInstance.marvelHeroesApi.getMarvelList(
            limit,
            offset,
            ts,
            apiKey,
            hash
        )
    // Get main page hero order by ascending or descending name
    // For descending pass "-name"
    suspend fun getMarvelHeroListOrderByName(name: String, ts: Long, apiKey: String, hash: String) =
        RetrofitInstance.marvelHeroesApi.getMarvelListOrderByName(
            name,
            ts,
            apiKey,
            hash
        )
}