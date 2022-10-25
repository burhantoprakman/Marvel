package com.spiderman.marvel.api


import com.spiderman.marvel.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        //Marvel Heroes api
        val marvelHeroesApi: MarvelHeroesAPI by lazy {
            retrofit.create(MarvelHeroesAPI::class.java)
        }

        //Marvel Heroes about list api
        val marvelHeroesAboutApi: MarvelHeroesAboutAPI by lazy {
            retrofit.create(MarvelHeroesAboutAPI::class.java)
        }

    }
}