package com.example.kkocel.marvel.network

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object Rx {
    var unitTestMode = false
}

fun <T> Observable<T>.applySchedulers(): Observable<T> = if (Rx.unitTestMode) this else
    subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
