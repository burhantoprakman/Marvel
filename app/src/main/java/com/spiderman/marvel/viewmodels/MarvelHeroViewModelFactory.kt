package com.spiderman.marvel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spiderman.marvel.repository.MarvelHeroRepository

@Suppress("UNCHECKED_CAST")
class MarvelHeroViewModelFactory(private val repository: MarvelHeroRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarvelHeroViewModel(repository) as T
    }
}