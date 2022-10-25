package com.spiderman.marvel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spiderman.marvel.repository.MarvelHeroAboutRepository

@Suppress("UNCHECKED_CAST")
class MarvelHeroAboutViewModelFactory(private val repository: MarvelHeroAboutRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarvelHeroAboutViewModel(repository) as T
    }
}