package com.example.kkocel.marvel.detail.state

import com.example.kkocel.marvel.network.model.ComicDetailModel
import com.example.kkocel.marvel.network.model.ImageModel
import com.example.kkocel.marvel.state.ViewState

class ComicDetailViewState(
        val id: Int?,
        val title: String?,
        val description: String?,
        val thumbnail: ImageModel?,
        val images: List<ImageModel>?) : ViewState {

    constructor(comicDetailModel: ComicDetailModel) : this(
            id = comicDetailModel.id,
            title = comicDetailModel.title,
            description = comicDetailModel.description,
            thumbnail = comicDetailModel.thumbnail,
            images = comicDetailModel.images)
}