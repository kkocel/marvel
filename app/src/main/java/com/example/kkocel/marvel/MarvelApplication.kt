package com.example.kkocel.marvel

import android.app.Application
import android.os.StrictMode
import com.squareup.leakcanary.LeakCanary

class MarvelApplication : Application() {

    companion object {
        var marvelApplication: MarvelApplication? = null
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        marvelApplication = this

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())
    }
}