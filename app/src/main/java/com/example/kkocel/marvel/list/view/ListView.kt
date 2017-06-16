package com.example.kkocel.marvel.list.view

import com.example.kkocel.marvel.list.state.CorrectListPageViewState
import com.example.kkocel.marvel.state.ConnectionErrorViewState

interface ListView {
    fun onPageLoaded(page: CorrectListPageViewState)
    fun onNetworkError(it: ConnectionErrorViewState)
    fun onUnrecoverableError(throwable: Throwable)
}
