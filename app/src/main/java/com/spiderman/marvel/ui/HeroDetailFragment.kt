package com.spiderman.marvel.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.spiderman.marvel.R
import com.spiderman.marvel.models.Url
import kotlinx.android.synthetic.main.fragment_hero_detail.*
import com.spiderman.marvel.models.*

class HeroDetailFragment : Fragment(R.layout.fragment_hero_detail), View.OnClickListener {
    private val args: HeroDetailFragmentArgs? by navArgs()

    // Get url link size to create button
    private var urlLinkSize = 0
    private lateinit var result: Result

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.webViewClient = WebViewClient()

        args?.let { heroDetailArgs ->
            heroDetailArgs.result?.urls?.let {
                urlLinkSize = it.size
                // Add button programmatically
                addButton(urlLinkSize, it)
            }
            val imageUrl = heroDetailArgs.result?.thumbnail?.path + "." +
                    heroDetailArgs.result?.thumbnail?.extension
            val comicSize = heroDetailArgs.result?.comics?.available.toString()
            val seriesSize = heroDetailArgs.result?.series?.available.toString()
            val storiesSize = heroDetailArgs.result?.stories?.available.toString()
            val eventsSize = heroDetailArgs.result?.events?.available.toString()

            Glide.with(this)
                .load(imageUrl)
                .into(iv_hero)
            tv_hero.text = heroDetailArgs.result?.name
            tv_comics.text = "Comics size :$comicSize"
            tv_series.text = "Series size :$seriesSize"
            tv_stories.text = "Stories Size:$storiesSize"
            tv_event.text = "Events size : $eventsSize"
            heroDetailArgs.result?.let {
                result = heroDetailArgs.result!!
                cl_data.visibility = View.VISIBLE
                tv_NoData.visibility = View.GONE
            }
        }

        tv_event.setOnClickListener(this)
        tv_comics.setOnClickListener(this)
        tv_series.setOnClickListener(this)
        tv_stories.setOnClickListener(this)
    }

    // Add button programmatically in listview
    private fun addButton(size: Int, list: List<Url>) {
        for (indices in 0 until size) {
            list[indices]
            val buttonDynamic = Button(context)
            buttonDynamic.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            buttonDynamic.text = list[indices].type
            buttonDynamic.setOnClickListener {
                webView.loadUrl(list[indices].url)
            }
            ll_links.addView(buttonDynamic)
        }
    }

    override fun onClick(view: View?) {
        // Get clicked button text
        val viewsText = (view as MaterialTextView).text.toString()

        //Set the result type with clicked button
        with(viewsText) {
            when {
                contains("Comics") -> result.listType = "comics"
                contains("Series") -> result.listType = "series"
                contains("Events") -> result.listType = "events"
                contains("Stories") -> result.listType = "stories"
            }
        }
        val action =
            HeroDetailFragmentDirections.actionHeroDetailFragmentToAboutHeroFragment(result)
        findNavController().navigate(action)
    }
}