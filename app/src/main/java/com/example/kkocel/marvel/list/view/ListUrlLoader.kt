package com.example.kkocel.marvel.list.view

import android.content.Context
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.example.kkocel.marvel.network.model.ImageModel

class ListUrlLoader(context: Context) : BaseGlideUrlLoader<ImageModel>(context) {

    override fun getUrl(model: ImageModel, width: Int, height: Int): String {
        return model.path + "/portrait_uncanny." + model.extension
    }
}
