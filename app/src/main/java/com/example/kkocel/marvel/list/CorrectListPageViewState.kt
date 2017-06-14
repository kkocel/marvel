package com.example.kkocel.marvel.list

import com.example.kkocel.marvel.Comic

data class CorrectListPageViewState(val page: Int, val comics: List<Comic>) : ListPageViewState {
}
