package com.spiderman.marvel.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.spiderman.marvel.R
import com.spiderman.marvel.models.*
import kotlinx.android.synthetic.main.about_info_view.view.*

class MarvelAboutAdapter : RecyclerView.Adapter<MarvelAboutAdapter.ListViewHolder>() {

    var result: Result? = null
    private var list: List<Item> = ArrayList()
    var resultList: List<Result> = ArrayList()
    var listType: String = ""

    inner class ListViewHolder(itemView: View) : ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.about_info_view,
                parent,
                false
            )
        )
    }

    //Click listener for recyclerview item
    private var onItemClickListener: ((Item) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.apply {
            when (listType) {
                "comics" -> list = result!!.comics.items
                "series" -> list = result!!.series.items
                "events" -> list = result!!.events.items
                "stories" -> list = result!!.stories.items
            }


            var writer = "No found"

            //To make expandable layout. When you click item it will expand
            val isExpandable: Boolean = list[position].expandable

            txt_name.text = list[position].name

            expandable_layout.visibility = if (isExpandable) View.VISIBLE else View.GONE
            setOnClickListener {
                //After click item again it will collapse again
                list[position].expandable = !list[position].expandable
                onItemClickListener?.let { it(list[position]) }
            }

            //If result is empty , dont set any value to view
            if (resultList.isNotEmpty()) {
                val imageUrl = resultList[0].thumbnail?.path + "." +
                        resultList[0].thumbnail?.extension
                if (resultList[0].creators?.items?.size != 0) {
                    writer = resultList[0].creators?.items?.get(0)?.name!!

                }
                Glide.with(context).load(imageUrl).into(iv_thumbnail)

                //Got 10 character to remove unnecessary date information
                tv_date.text = "Date: " + resultList[0].modified?.take(10)
                tv_writer.text = "Writer : $writer"
            } else {
                tv_date.text = ""
                tv_writer.text = ""
            }
        }
    }


    override fun getItemCount(): Int {
        return when (listType) {
            "comics" -> result!!.comics.items.size
            "series" -> result!!.series.items.size
            "stories" -> result!!.stories.items.size
            "events" -> result!!.events.items.size
            else -> {
                0
            }
        }
    }

    //Click listener function to reach it from fragment
    fun setOnItemClickListener(listener: (Item?) -> Unit) {
        onItemClickListener = listener
    }

}