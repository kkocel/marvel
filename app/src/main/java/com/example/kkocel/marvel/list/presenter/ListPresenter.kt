package com.example.kkocel.marvel.list.presenter

import com.example.kkocel.marvel.list.state.CorrectListPageViewState
import com.example.kkocel.marvel.list.view.ListActivity
import com.example.kkocel.marvel.network.MarvelRepository
import com.example.kkocel.marvel.list.view.ListView
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ListPresenter(val listView: ListView, val marvelRepository: MarvelRepository) {
    private val CAPTAIN_AMERICA = 1009220
    private val INITIAL_OFFSET = 0
    private val ITEMS = 100

    private var subscriptions = CompositeDisposable()
    private val pageRequests: PublishSubject<Int> = PublishSubject.create()

    fun onCreate() {
        subscriptions.add(pageRequests.startWith(INITIAL_OFFSET)
                .flatMap { page ->
                    marvelRepository.getComicsForCharacter(CAPTAIN_AMERICA, page, ITEMS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            when {
                                it is CorrectListPageViewState -> listView.onPageLoaded(it)
                                else -> listView.onNetworkError(it as ConnectionErrorViewState)
                            }
                        },
                        { e -> listView.onUnrecoverableError(e) }
                ))
    }

    // TODO: Handle situations where internet is available but server is down
    fun retryWhenNetworkIsAvailable(currentPage: Int) {
        subscriptions.add(ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isConnectedToInternet ->
                    if (isConnectedToInternet) {
                        retryCurrentPage(ListActivity.pageToOffset(currentPage))
                    }
                }))
    }

    private fun retryCurrentPage(currentOffset: Int) = pageRequests.onNext(currentOffset)

    fun requestNextPage(currentOffset: Int) = pageRequests.onNext(currentOffset + ITEMS)

    fun forceReload() = pageRequests.onNext(INITIAL_OFFSET)

    fun onViewDestroyed() = subscriptions.dispose()
}