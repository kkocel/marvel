package com.example.kkocel.marvel.network

import com.example.kkocel.marvel.state.ViewState
import io.reactivex.Observable

interface MarvelRepository {

    fun getComicsForCharacter(
            characterId: Int,
            offset: Int,
            limit: Int
    ): Observable<ViewState>

    fun getComicDetails(
            comicId: Int
    ): Observable<ViewState>
}

