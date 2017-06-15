package com.example.kkocel.marvel.detail.di

import com.example.kkocel.marvel.detail.presenter.DetailPresenter
import com.example.kkocel.marvel.detail.view.DetailView
import com.example.kkocel.marvel.di.BaseModule
import com.example.kkocel.marvel.network.rest.RetrofitModule

class DetailModule(retrofitModule: RetrofitModule) : BaseModule(retrofitModule) {

    fun provideListPresenter(detailView: DetailView): DetailPresenter {
        return DetailPresenter(detailView, provideMarvelRepository())
    }
}