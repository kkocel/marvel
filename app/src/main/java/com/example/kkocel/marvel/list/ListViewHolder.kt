package com.example.kkocel.marvel.list;

import android.content.Intent
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.bumptech.glide.Glide
import com.example.kkocel.marvel.Comic
import com.example.kkocel.marvel.ComicDetailActivity
import kotlinx.android.synthetic.main.comic_list_content.view.*

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindComic(comic: Comic) {
        val context = itemView.context

        itemView.comic_name.text = comic.title

        Glide.with(context)
                .using(ListUrlLoader(context))
                .load(comic.imageUrl)
                .into(itemView.comic_thumbnail)

        itemView.setOnClickListener({
            context.startActivity(
                    Intent(context, ComicDetailActivity::class.java)
                            .putExtra(ComicDetailActivity.COMIC_ID, comic.id))
        })
    }
}