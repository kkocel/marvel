package com.example.kkocel.marvel.network.model

data class ComicDetailModel(
        val id: Int?,
        val title: String?,
        val description: String?,
        val thumbnail: ImageModel?,
        val images: List<ImageModel>?)