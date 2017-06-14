package com.example.kkocel.marvel.network.rest

import com.example.kkocel.marvel.network.model.ComicModel
import com.example.kkocel.marvel.network.model.DataWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApiService {

    @GET("characters")
    fun getComicsForCharacter(
            @Query("offset") offset: Int?,
            @Query("limit") limit: Int?
    ): Single<DataWrapper<List<ComicModel>>>
}
