package com.example.kkocel.marvel.list.mvp

import com.example.kkocel.marvel.list.CorrectListPageViewState
import com.example.kkocel.marvel.model.Comic
import com.example.kkocel.marvel.network.rest.MarvelApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
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
                .flatMap { page -> marvelApiService.getComicsForCharacter(CAPITAN_AMERICA, page, ITEMS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { wrapper ->
                            listView.onPageLoaded(CorrectListPageViewState(wrapper.data?.offset ?: 0, wrapper.data?.results?.map { Comic(it) } ?: emptyList()))
                        },
                        { e -> listView.onUnrecoverableError(e) }
                )

        subscriptions.add(disposable)

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