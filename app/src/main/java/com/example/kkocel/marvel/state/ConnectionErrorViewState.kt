package com.example.kkocel.marvel.state

import com.example.kkocel.marvel.state.ViewState

data class ConnectionErrorViewState(val error: Throwable) : ViewState