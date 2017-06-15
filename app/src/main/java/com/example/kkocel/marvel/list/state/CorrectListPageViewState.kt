package com.example.kkocel.marvel.list.state

import com.example.kkocel.marvel.model.Comic
import com.example.kkocel.marvel.state.ViewState

data class CorrectListPageViewState(val offset: Int, val comics: List<Comic>) : ViewState