package com.spiderman.marvel.api

import com.spiderman.marvel.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelHeroesAboutAPI {

    @GET("comics/{comicId}")
    suspend fun getComicList(
        @Path("comicId")
        comicId : Int,
        @Query("ts")
        ts : Long,
        @Query("apikey")
        apiKey : String ,
        @Query("hash")
        hash : String
    ) : Response<MarvelHeroResponse>

    @GET("stories/{storyId}")
    suspend fun getStoriesList(
        @Path("storyId")
        storyId : Int,
        @Query("ts")
        ts : Long,
        @Query("apikey")
        apiKey : String ,
        @Query("hash")
        hash : String

    ) : Response<MarvelHeroResponse>

    @GET("series/{seriesId}")
    suspend fun getSeriesList(
        @Path("seriesId")
        seriesId : Int,
        @Query("ts")
        ts : Long,
        @Query("apikey")
        apiKey : String ,
        @Query("hash")
        hash : String
    ) : Response<MarvelHeroResponse>

    @GET("events/{eventId}")
    suspend fun getEventsList(
        @Path("eventId")
        eventId : Int,
        @Query("ts")
        ts : Long,
        @Query("apikey")
        apiKey : String ,
        @Query("hash")
        hash : String

    ) : Response<MarvelHeroResponse>

}