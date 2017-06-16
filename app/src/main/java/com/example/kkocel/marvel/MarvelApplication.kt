package com.example.kkocel.marvel

import android.app.Application

class MarvelApplication : Application() {

    companion object {
        var marvelApplication: MarvelApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        marvelApplication = this
    }
}