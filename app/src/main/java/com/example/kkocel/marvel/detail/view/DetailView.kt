package com.example.kkocel.marvel.detail.view

import com.example.kkocel.marvel.detail.state.ComicDetailViewState
import com.example.kkocel.marvel.state.ConnectionErrorViewState

interface DetailView {
    fun onDetailsLoaded(detailViewState: ComicDetailViewState)
    fun onNetworkError(detailViewState: ConnectionErrorViewState)
    fun onUnrecoverableError(e: Throwable)

}
