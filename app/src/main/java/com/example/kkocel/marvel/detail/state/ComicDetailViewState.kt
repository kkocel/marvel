package com.example.kkocel.marvel.detail.state

import com.example.kkocel.marvel.MarvelApplication
import com.example.kkocel.marvel.R
import com.example.kkocel.marvel.network.model.ComicDetailModel
import com.example.kkocel.marvel.network.model.ImageModel
import com.example.kkocel.marvel.state.ViewState

class ComicDetailViewState(
        val id: Int?,
        val title: String?,
        val description: String?,
        val thumbnail: ImageModel?,
        val image: ImageModel?) : ViewState {

    constructor(comicDetailModel: ComicDetailModel) : this(
            id = comicDetailModel.id,
            title = comicDetailModel.title ?: MarvelApplication.marvelApplication?.getString(R.string.emptyTitle),
            description = comicDetailModel.description ?: MarvelApplication.marvelApplication?.getString(R.string.emptyDescription),
            thumbnail = comicDetailModel.thumbnail,
            image = comicDetailModel.images?.firstOrNull() ?: comicDetailModel.thumbnail)
}