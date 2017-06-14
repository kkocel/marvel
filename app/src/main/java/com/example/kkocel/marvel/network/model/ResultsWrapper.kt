package com.example.kkocel.marvel.network.model

class ResultsWrapper<T> {
    var results: T? = null
    var offset: Int? = null
    var total: Int? = null
    var count: Int? = null
}