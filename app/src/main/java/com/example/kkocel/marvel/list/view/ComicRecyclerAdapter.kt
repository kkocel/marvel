package com.example.kkocel.marvel.list.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kkocel.marvel.R
import com.example.kkocel.marvel.model.Comic

internal class ComicRecyclerAdapter : RecyclerView.Adapter<ListViewHolder>() {
    private var comics = mutableListOf<Comic>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comic_list_content, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindComic(comics[position])
    }

    override fun getItemId(position: Int): Long {
        return comics[position].id?.toLong() ?: 0L
    }

    override fun getItemCount(): Int {
        return comics.size
    }

    fun swapList(comics: List<Comic>?) {
        if (comics != null) this.comics.addAll(comics)
        notifyDataSetChanged()
    }
}

