package com.example.kkocel.marvel.list

import com.example.kkocel.marvel.model.Comic

data class CorrectListPageViewState(val offset: Int, val comics: List<Comic>) : ListPageViewState