package com.spiderman.marvel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spiderman.marvel.models.MarvelHeroResponse
import com.spiderman.marvel.repository.MarvelHeroRepository
import com.spiderman.marvel.resources.Resources
import com.spiderman.marvel.util.Constants
import com.spiderman.marvel.util.Constants.Companion.API_KEY
import com.spiderman.marvel.util.Constants.Companion.DEFAULT_REQUEST_LIST_LIMIT
import com.spiderman.marvel.util.Hashing
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

class MarvelHeroViewModel(private val repository: MarvelHeroRepository) : ViewModel() {
    //Observable list for MarvelHeroFragment
    val marvelHeroList: MutableLiveData<Resources<MarvelHeroResponse>> = MutableLiveData()

    //Response for check pagination
    var marvelHeroesResponse: MarvelHeroResponse? = null

    // Timestamp value to pass request
    private var ts = 0L

    //Hashable string
    private var hash = ""

    //Hashed string
    private var passhash = ""

    // Offset value for request
    var offset = 0

    //Default page number = 1
    var pagenumber = 1


    init {
        hashValue()
        getMarvelHeroList()
    }

    fun getMarvelHeroList() = viewModelScope.launch {
        marvelHeroList.postValue(Resources.Loading())
        val response =
            repository.getMarvelHeroList(DEFAULT_REQUEST_LIST_LIMIT, offset, ts, API_KEY, passhash)
        marvelHeroList.postValue(handleMarvelList(response))
    }

    // No need while using pagination
    /*fun getMarvelHeroListOrderByNameAscending() = viewModelScope.launch {

        marvelHeroList.postValue(Resources.Loading())
        val response = repository.getMarvelHeroListOrderByName("name", ts, API_KEY, passhash)
        marvelHeroList.postValue(handleMarvelList(response))
    }*/

    // No need while using pagination
    /*fun getMarvelHeroListOrderByNameDescending() = viewModelScope.launch {
        marvelHeroList.postValue(Resources.Loading())
        val response = repository.getMarvelHeroListOrderByName("-name", ts, API_KEY, passhash)
        marvelHeroList.postValue(handleMarvelList(response))
    }*/

    private fun handleMarvelList(response: Response<MarvelHeroResponse>)
            : Resources<MarvelHeroResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                //Increase page number after each response
                pagenumber++

                offset += 30
                //If its our first time to got data
                if (marvelHeroesResponse == null) {
                    marvelHeroesResponse = resultResponse
                } else {
                    //Add new facts list from other page to old list
                    val oldMarvelList = marvelHeroesResponse?.data?.results
                    val newMarvelList = resultResponse.data.results
                    oldMarvelList?.addAll(newMarvelList)
                }

                return Resources.Success(marvelHeroesResponse ?: resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
    //Prepare timestamp and hash value for request
    private fun hashValue() {
        ts = Date().time
        hash = ts.toString() + Constants.PRIVATE_KEY + API_KEY
        passhash = Hashing.getHash(hash)
    }

}