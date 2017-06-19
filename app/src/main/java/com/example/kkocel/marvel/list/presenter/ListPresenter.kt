package com.example.kkocel.marvel.list.presenter

import com.example.kkocel.marvel.list.state.CorrectListPageViewState
import com.example.kkocel.marvel.list.view.ListView
import com.example.kkocel.marvel.network.MarvelRepository
import com.example.kkocel.marvel.network.applySchedulers
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class ListPresenter(val listView: ListView, val marvelRepository: MarvelRepository) {
    private val BEN_PARKER = 1009489
    private val INITIAL_OFFSET = 0
    private val ITEMS = 100

    private var subscriptions = CompositeDisposable()
    private val pageRequests: PublishSubject<Int> = PublishSubject.create()

    fun onCreate() {
        subscriptions.add(pageRequests.startWith(INITIAL_OFFSET)
                .flatMap { page ->
                    marvelRepository.getComicsForCharacter(BEN_PARKER, page, ITEMS)
                            .applySchedulers()
                }
                .applySchedulers()
                .subscribeBy(
                        onNext = {
                            when (it) {
                                is CorrectListPageViewState -> listView.onPageLoaded(it)
                                else -> listView.onNetworkError(it as ConnectionErrorViewState)
                            }
                        },
                        onError = { e -> listView.onUnrecoverableError(e) }
                ))
    }

    // TODO: Handle situations where internet is available but server is down
    fun retryWhenNetworkIsAvailable(currentPage: Int) {
        subscriptions.add(ReactiveNetwork.observeInternetConnectivity()
                .applySchedulers()
                .subscribeBy(
                        onNext = { isConnectedToInternet ->
                            if (isConnectedToInternet) {
                                retryCurrentPage(pageToOffset(currentPage))
                            }
                        }))
    }

    private fun retryCurrentPage(currentOffset: Int) = pageRequests.onNext(currentOffset)

    fun requestNextPage(currentOffset: Int) = pageRequests.onNext(currentOffset + ITEMS)

    fun forceReload() = pageRequests.onNext(INITIAL_OFFSET)

    fun onViewDestroyed() = subscriptions.dispose()

    companion object {
        @JvmStatic
        fun pageToOffset(page: Int) = page * 100
    }
}