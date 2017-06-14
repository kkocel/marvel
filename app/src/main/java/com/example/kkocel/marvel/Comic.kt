package com.example.kkocel.marvel

import com.example.kkocel.marvel.network.model.ComicModel

data class Comic(val id: Int, val title: String, val imageUrl: String) {

    constructor(comicModel: ComicModel) : this(id = comicModel.id, title = comicModel.title, imageUrl = comicModel.imageUrl)
}