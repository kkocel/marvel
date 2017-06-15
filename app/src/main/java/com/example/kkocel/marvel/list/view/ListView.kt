package com.example.kkocel.marvel.list.view

import com.example.kkocel.marvel.state.ViewState

interface ListView {
    fun onPageLoaded(page: ViewState)
    fun onUnrecoverableError(throwable: Throwable)
}
