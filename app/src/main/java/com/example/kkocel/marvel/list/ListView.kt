package com.example.kkocel.marvel.list

interface ListView {
    fun onPageLoaded(page: ListPageViewState)
    fun onUnrecoverableError(throwable: Throwable)
}
