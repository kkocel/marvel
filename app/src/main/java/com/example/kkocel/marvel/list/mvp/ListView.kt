package com.example.kkocel.marvel.list.mvp

import com.example.kkocel.marvel.list.ListPageViewState

interface ListView {
    fun onPageLoaded(page: ListPageViewState)
    fun onUnrecoverableError(throwable: Throwable)
}
