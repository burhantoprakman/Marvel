package com.spiderman.marvel.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.spiderman.marvel.R
import com.spiderman.marvel.adapters.MarvelAboutAdapter
import com.spiderman.marvel.resources.Resources
import com.spiderman.marvel.viewmodels.MarvelHeroAboutViewModel
import kotlinx.android.synthetic.main.fragment_about_hero.*
import com.spiderman.marvel.models.*

@Suppress("NAME_SHADOWING")
class AboutHeroFragment : Fragment(R.layout.fragment_about_hero) {

    private val args: AboutHeroFragmentArgs? by navArgs()

    //What kind of list we will show , Comics,Series etc
    lateinit var listType: String

    lateinit var viewModel: MarvelHeroAboutViewModel
    lateinit var marvelAboutAdapter: MarvelAboutAdapter
    private var resultList: Result? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).marvelAboutHeroViewModel

        args?.let {
            listType = it.aboutType?.listType.toString()
            resultList = it.aboutType

        }

        setUpAdapter()
        marvelAboutAdapter.listType = listType
        marvelAboutAdapter.result = resultList
        marvelAboutAdapter.notifyDataSetChanged()

        marvelAboutAdapter.setOnItemClickListener { item ->
            //Get last 7 character
            val id = item?.resourceURI?.takeLast(7)
            //Get the characters after "/" to find what the comicId , seriesId , eventsId or storiesId
            val passId = id?.substringAfter("/")?.toInt()
            passId?.let {
                viewModel.getRequestType(it)
            }
            when (listType) {
                "comics" -> {
                    viewModel.getComicsList()
                    getAboutList()
                    marvelAboutAdapter.notifyDataSetChanged()
                }
                "series" -> {
                    viewModel.getSeriesList()
                    getAboutList()
                    marvelAboutAdapter.notifyDataSetChanged()
                }
                "stories" -> {
                    viewModel.getStoriesList()
                    getAboutList()
                    marvelAboutAdapter.notifyDataSetChanged()
                }
                "events" -> {
                    viewModel.getEventsList()
                    getAboutList()
                    marvelAboutAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setUpAdapter() {
        marvelAboutAdapter = MarvelAboutAdapter()
        rv_marvel_about.apply {
            adapter = marvelAboutAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getAboutList() {
        viewModel.list.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { response ->

                        marvelAboutAdapter.resultList = response.data.results
                        marvelAboutAdapter.notifyDataSetChanged()
                    }
                }
                is Resources.Error -> {
                    response.message?.let {
                        Snackbar.make(requireView(), "Error: \n$it", Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading -> {
                }
                else -> {
                    // DO NOTHING
                }
            }
        }

    }
}