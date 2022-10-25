package com.spiderman.marvel.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.spiderman.marvel.R
import com.spiderman.marvel.repository.MarvelHeroAboutRepository
import com.spiderman.marvel.repository.MarvelHeroRepository
import com.spiderman.marvel.viewmodels.MarvelHeroAboutViewModel
import com.spiderman.marvel.viewmodels.MarvelHeroAboutViewModelFactory
import com.spiderman.marvel.viewmodels.MarvelHeroViewModel
import com.spiderman.marvel.viewmodels.MarvelHeroViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //Viewmodels creations
    lateinit var marvelListViewModel: MarvelHeroViewModel
    lateinit var marvelAboutHeroViewModel: MarvelHeroAboutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNagivationAndActionBar()

        //MarvelHeroViewModel create
        val marvelListRepository = MarvelHeroRepository()
        val marvelListFactory = MarvelHeroViewModelFactory(marvelListRepository)
        marvelListViewModel =
            ViewModelProvider(this, marvelListFactory)[MarvelHeroViewModel::class.java]

        //MarvelAboutHeroViewModel create
        val marvelAboutRepository = MarvelHeroAboutRepository()
        val marvelAboutFactory = MarvelHeroAboutViewModelFactory(marvelAboutRepository)
        marvelAboutHeroViewModel =
            ViewModelProvider(this, marvelAboutFactory)[MarvelHeroAboutViewModel::class.java]

    }

    fun setupNagivationAndActionBar() {
        //Bottom navigation bar and controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        //Created app config from nav controller graph and added it into action var
        // Shows fragments title
        val appBarConfiguration = AppBarConfiguration(bottomNavigationView.menu)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}