package com.spiderman.marvel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spiderman.marvel.R
import com.spiderman.marvel.models.*
import kotlinx.android.synthetic.main.hero_item_view.view.*

class MarvelHeroAdapter : RecyclerView.Adapter<MarvelHeroAdapter.MarvelHeroViewHolder>() {

    var list: List<Result> = ArrayList()

    inner class MarvelHeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            // Each character has unique id
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelHeroViewHolder {
        return MarvelHeroViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.hero_item_view,
                parent,
                false
            )
        )
    }

    //Click listener for recyclerview item
    private var onItemClickListener: ((Result) -> Unit)? = null

    override fun onBindViewHolder(holder: MarvelHeroViewHolder, position: Int) {
        val result = differ.currentList[position]
        //Get image url from result
        val imageUrl = result.thumbnail?.path +"." +
                result.thumbnail?.extension

        holder.itemView.apply {
            Glide.with(context)
                .load(imageUrl)
                .into(iv_heroIcon)
            tv_heroName.text = result.name
            tv_attributes.text = result.description
            setOnClickListener {
                onItemClickListener?.let { it(result) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    //Click listener function to reach it from fragment
    fun setOnItemClickListener(listener: (com.spiderman.marvel.models.Result) -> Unit) {
        onItemClickListener = listener
    }
}