package com.example.kkocel.marvel.network

import com.example.kkocel.marvel.detail.state.ComicDetailViewState
import com.example.kkocel.marvel.list.state.CorrectListPageViewState
import com.example.kkocel.marvel.model.Comic
import com.example.kkocel.marvel.network.rest.MarvelApiService
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import com.example.kkocel.marvel.state.ViewState
import io.reactivex.Observable

// TODO: Implement caching here in DB
class NetworkOnlyMavenRepository(val marvelApiService: MarvelApiService) : MarvelRepository {
    override fun getComicsForCharacter(characterId: Int, offset: Int, limit: Int): Observable<ViewState> =
            marvelApiService.getComicsForCharacter(characterId, offset, limit).map {
                when {
                    !it.isError -> (CorrectListPageViewState(
                            it.response()?.body()?.data?.offset ?: 0,
                            it.response()?.body()?.data?.results?.map { Comic(it) } ?: emptyList()))
                    else -> (ConnectionErrorViewState(it.error()!!))
                }
            }

    override fun getComicDetails(comicId: Int): Observable<ViewState> =
            marvelApiService.getComicDetails(comicId).map {
                when {
                    it.isError -> ConnectionErrorViewState(it.error()!!)
                    else -> ComicDetailViewState(it.response()?.body()?.data?.results?.first()!!)
                }
            }
}
