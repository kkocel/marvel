package com.example.kkocel.marvel.detail.view

import com.example.kkocel.marvel.state.ViewState

interface DetailView {
    fun onDetailsLoaded(detailViewState: ViewState)
    fun onUnrecoverableError(e: Throwable)

}
