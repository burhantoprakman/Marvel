package com.spiderman.marvel.repository

import com.spiderman.marvel.api.RetrofitInstance

class MarvelHeroAboutRepository {

    suspend fun getComicsData(comicId: Int, ts: Long, apiKey: String, hash: String) =
        RetrofitInstance.marvelHeroesAboutApi.getComicList(comicId, ts, apiKey, hash)

    suspend fun getSeriesData(seriesId: Int, ts: Long, apiKey: String, hash: String) =
        RetrofitInstance.marvelHeroesAboutApi.getSeriesList(seriesId, ts, apiKey, hash)

    suspend fun getStoriesData(storiesId: Int, ts: Long, apiKey: String, hash: String) =
        RetrofitInstance.marvelHeroesAboutApi.getStoriesList(storiesId, ts, apiKey, hash)

    suspend fun getEventsData(eventId: Int, ts: Long, apiKey: String, hash: String) =
        RetrofitInstance.marvelHeroesAboutApi.getEventsList(eventId, ts, apiKey, hash)

}