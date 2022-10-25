package com.spiderman.marvel.api

import com.spiderman.marvel.models.MarvelHeroResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelHeroesAPI {
    @GET("characters")
    suspend fun getMarvelList(
        @Query("limit")
        limit : Int ,
        @Query("offset")
        offset : Int ,
        @Query("ts")
        ts : Long,
        @Query("apikey")
        apiKey : String ,
        @Query("hash")
        hash : String

    ) : Response<MarvelHeroResponse>

    @GET("characters?")
    suspend fun getMarvelListOrderByName(
        @Query("orderBy")
        name : String,
        @Query("ts")
        ts : Long,
        @Query("apikey")
        apiKey : String,
        @Query("hash")
        hash : String
    ) : Response<MarvelHeroResponse>
}