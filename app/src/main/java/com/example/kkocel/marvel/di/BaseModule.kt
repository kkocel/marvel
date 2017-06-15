package com.example.kkocel.marvel.di

import com.example.kkocel.marvel.network.MarvelRepository
import com.example.kkocel.marvel.network.NetworkOnlyMavenRepository
import com.example.kkocel.marvel.network.rest.MarvelApiService
import com.example.kkocel.marvel.network.rest.RetrofitModule

open class BaseModule(val retrofitModule: RetrofitModule) {
    companion object {
        var marvelRepository: MarvelRepository? = null
    }

    fun provideMarvelApiService(): MarvelApiService {
        return retrofitModule.provideRetrofit().create(MarvelApiService::class.java)
    }

    fun provideMarvelRepository(): MarvelRepository {
        return marvelRepository ?: NetworkOnlyMavenRepository(provideMarvelApiService())
    }

}