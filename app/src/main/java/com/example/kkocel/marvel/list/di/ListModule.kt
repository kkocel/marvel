package com.example.kkocel.marvel.list.di

import com.example.kkocel.marvel.di.BaseModule
import com.example.kkocel.marvel.network.rest.RetrofitModule
import com.example.kkocel.marvel.list.presenter.ListPresenter
import com.example.kkocel.marvel.list.view.ListView

class ListModule(retrofitModule: RetrofitModule) : BaseModule(retrofitModule) {

    fun provideListPresenter(listView: ListView): ListPresenter {
        return ListPresenter(listView, provideMarvelRepository())
    }
}