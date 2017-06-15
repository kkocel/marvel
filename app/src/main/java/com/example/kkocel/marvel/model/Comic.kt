package com.example.kkocel.marvel.model

import com.example.kkocel.marvel.network.model.ComicModel
import com.example.kkocel.marvel.network.model.ImageModel

data class Comic(val id: Int?, val title: String?, val imageUrl: ImageModel?) {

    constructor(comicModel: ComicModel) : this(id = comicModel.id, title = comicModel.title, imageUrl = comicModel.thumbnail)
}