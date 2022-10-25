package com.spiderman.marvel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spiderman.marvel.models.*
import com.spiderman.marvel.repository.MarvelHeroAboutRepository
import com.spiderman.marvel.resources.Resources
import com.spiderman.marvel.util.Constants.Companion.API_KEY
import com.spiderman.marvel.util.Constants.Companion.PRIVATE_KEY
import com.spiderman.marvel.util.Hashing
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Date

class MarvelHeroAboutViewModel(private val repository: MarvelHeroAboutRepository) : ViewModel() {

    //Observable list for MarvelAboutFragment
    val list: MutableLiveData<Resources<MarvelHeroResponse>> = MutableLiveData()

    //It defines what kind of id that we will send
    // comicId,seriesId,eventId,storiesId
    private var requestId: Int = 0

    private var ts = 0L
    private var hash = ""
    private var passhash = ""

    init {
        hashValue()
    }

    //Get request type from view class
    fun getRequestType(id: Int) {
        requestId = id
    }

    fun getComicsList() = viewModelScope.launch {
        list.postValue(Resources.Loading())
        val response = repository.getComicsData(requestId, ts, API_KEY, passhash)
        list.postValue(handleList(response))
    }

    fun getSeriesList() = viewModelScope.launch {
        list.postValue(Resources.Loading())
        val response = repository.getSeriesData(requestId, ts, API_KEY, passhash)
        list.postValue(handleList(response))
    }

    fun getStoriesList() = viewModelScope.launch {
        list.postValue(Resources.Loading())
        val response = repository.getStoriesData(requestId, ts, API_KEY, passhash)
        list.postValue(handleList(response))
    }

    fun getEventsList() = viewModelScope.launch {
        list.postValue(Resources.Loading())
        val response = repository.getEventsData(requestId, ts, API_KEY, passhash)
        list.postValue(handleList(response))
    }

    private fun <T : Any> handleList(response: Response<T>): Resources<T> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun hashValue() {
        ts = Date().time
        hash = ts.toString() + PRIVATE_KEY + API_KEY
        passhash = Hashing.getHash(hash)
    }
}