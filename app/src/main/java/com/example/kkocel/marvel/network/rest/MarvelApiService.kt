package com.example.kkocel.marvel.network.rest

import com.example.kkocel.marvel.network.model.ComicModel
import com.example.kkocel.marvel.network.model.DataWrapper
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApiService {

    @GET("comics")
    fun getComicsForCharacter(
            @Query("characters") characterId: Int,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int
    ): Observable<Result<DataWrapper<List<ComicModel>>>>
}
