package com.example.kkocel.marvel.list

import android.content.Context
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader

class ListUrlLoader(context: Context) : BaseGlideUrlLoader<String>(context) {

    override fun getUrl(model: String, width: Int, height: Int): String {
        return model;
    }
}
