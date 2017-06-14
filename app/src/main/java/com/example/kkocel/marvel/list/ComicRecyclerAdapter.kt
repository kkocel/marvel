package com.example.kkocel.marvel.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kkocel.marvel.Comic
import com.example.kkocel.marvel.R

internal class ComicRecyclerAdapter : RecyclerView.Adapter<ListViewHolder>() {
    private var comics = emptyList<Comic>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comic_list_content, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindComic(comics[position])
    }

    override fun getItemId(position: Int): Long {
        return comics[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return comics.size
    }

    fun swapList(comics: List<Comic>?) {
        this.comics = comics ?: emptyList<Comic>()
        notifyDataSetChanged()
    }

}

