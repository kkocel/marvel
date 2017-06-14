package com.example.kkocel.marvel.list.di

import com.example.kkocel.marvel.list.mvp.ListPresenter
import com.example.kkocel.marvel.list.mvp.ListView
import com.example.kkocel.marvel.network.rest.MarvelApiService
import com.example.kkocel.marvel.network.rest.RetrofitModule

class ListModule(val retrofitModule: RetrofitModule) {

    companion object {
        var marvelApiService: MarvelApiService? = null
    }

    fun provideMarvelApiService(): MarvelApiService {
        return marvelApiService ?: retrofitModule.provideRetrofit().create(MarvelApiService::class.java)
    }

    fun provideListPresenter(listView: ListView): ListPresenter{
        return ListPresenter(listView, provideMarvelApiService())
    }
}