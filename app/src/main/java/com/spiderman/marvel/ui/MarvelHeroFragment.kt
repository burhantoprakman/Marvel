package com.spiderman.marvel.ui

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.spiderman.marvel.R
import com.spiderman.marvel.adapters.MarvelHeroAdapter
import com.spiderman.marvel.resources.Resources
import com.spiderman.marvel.util.Constants.Companion.DEFAULT_REQUEST_LIST_LIMIT
import com.spiderman.marvel.viewmodels.MarvelHeroViewModel
import kotlinx.android.synthetic.main.fragment_marvel_hero.*
import com.spiderman.marvel.models.*

class MarvelHeroFragment : Fragment(R.layout.fragment_marvel_hero) {

    lateinit var viewModel: MarvelHeroViewModel
    lateinit var marvelHeroAdapter: MarvelHeroAdapter
    lateinit var list: List<com.spiderman.marvel.models.Result>

    //If its already order by name ascending
    var orderByNameAscending = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()

        //Call view model
        viewModel = (activity as MainActivity).marvelListViewModel

        //Observe list from viewmodel
        viewModel.marvelHeroList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { marvelHeroListResponse ->
                        //TODO There is something wrong in here with diffutil logic.Fix this
                        marvelHeroAdapter.differ.submitList(null)

                        marvelHeroAdapter.differ.submitList(marvelHeroListResponse.data.results)
                        list = marvelHeroListResponse.data.results

                        //Calculate total page
                        val totalPage =
                            marvelHeroListResponse.data.total / DEFAULT_REQUEST_LIST_LIMIT + 2

                        //Check if its last page
                        isLastPage = viewModel.pagenumber == totalPage
                        if (isLastPage) {
                            //If its last page remove padding from recyclerview
                            rv_marvel_heroes.setPadding(0, 0, 0, 0)
                        }

                    }
                    hideProgressBar()
                }
                is Resources.Error -> {
                    response.message?.let {
                        Snackbar.make(requireView(), "Error: \n$it", Snackbar.LENGTH_SHORT).show()
                        hideProgressBar()
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
                else -> {
                    // DO NOTHING
                }
            }
        }

        fb_orderBy.setOnClickListener {
            if (orderByNameAscending) {
                fb_orderBy.text = "Order By Descending"
                orderByNameAscending = false;
                // Sort list by name
                val ascendingList = list.sortedBy { it.name }
                marvelHeroAdapter.differ.submitList(ascendingList)

                // No need to call this method for now. Keep it for future
                // viewModel.getMarvelHeroListOrderByNameAscending()

            } else {
                fb_orderBy.text = "Order By Ascending"
                orderByNameAscending = true;
                val descendingList = list.sortedByDescending { it.name }
                marvelHeroAdapter.differ.submitList(descendingList)

                // No need to call this method for now. Keep it for future
                // viewModel.getMarvelHeroListOrderByNameDescending()
            }

        }
        //Search view settings
        //This fun allow us to skip click search icon on searchview
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { filter(it) }
                return false
            }

        })

        marvelHeroAdapter.setOnItemClickListener {
            //Nav component action with arguments
            val action =
                MarvelHeroFragmentDirections.actionMarvelHeroFragmentToHeroDetailFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun setUpAdapter() {
        marvelHeroAdapter = MarvelHeroAdapter()
        rv_marvel_heroes.setHasFixedSize(true)
        rv_marvel_heroes.apply {
            adapter = marvelHeroAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@MarvelHeroFragment.scrollListener)

        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Result> = ArrayList()

        // running a for loop to compare elements.
        for (item in this.list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name.contains(text)) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Snackbar.make(requireView(), "No Data Found..", Snackbar.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            marvelHeroAdapter.differ.submitList(filteredlist)

        }
    }

    /*
        *Manual pagination implementation
     */

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    //Scroll listener for listen adapter's scroll
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        /*If its not loading, last page , lastItem, not first item , total more than visible
         and scrolling then make a request for get next page's data else stop making request*/
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= DEFAULT_REQUEST_LIST_LIMIT
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getMarvelHeroList()
                isScrolling = false
            }
        }
    }
}