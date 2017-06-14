package com.example.kkocel.marvel.list.mvp

import com.example.kkocel.marvel.list.CorrectListPageViewState
import com.example.kkocel.marvel.list.ConnectionErrorListViewState
import com.example.kkocel.marvel.model.Comic
import com.example.kkocel.marvel.network.rest.MarvelApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ListPresenter(val listView: ListView, val marvelApiService: MarvelApiService) {
    private val CAPITAN_AMERICA = 1009220
    private val INITIAL_OFFSET = 0
    private val ITEMS = 100
    private var subscriptions = CompositeDisposable()
    private val pageRequests: PublishSubject<Int> = PublishSubject.create()

    fun onCreate() {
        val disposable = pageRequests.startWith(INITIAL_OFFSET)
                .flatMap { page ->
                    marvelApiService.getComicsForCharacter(CAPITAN_AMERICA, page, ITEMS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            when {
                                !result.isError -> listView.onPageLoaded(CorrectListPageViewState(result.response()?.body()?.data?.offset ?: 0, result.response()?.body()?.data?.results?.map { Comic(it) } ?: emptyList()))
                                else -> listView.onPageLoaded(ConnectionErrorListViewState(result.error()!!))
                            }
                        },
                        { e -> listView.onUnrecoverableError(e) }
                )

        subscriptions.add(disposable)

    }

    fun retryCurrentPage(currentOffset: Int) {
        pageRequests.onNext(currentOffset)
    }

    fun requestNextPage(currentOffset: Int) {
        pageRequests.onNext(currentOffset + ITEMS)
    }

    fun forceReload() {
        pageRequests.onNext(INITIAL_OFFSET)
    }

    fun onViewDestroyed() {
        subscriptions.dispose()
    }
}