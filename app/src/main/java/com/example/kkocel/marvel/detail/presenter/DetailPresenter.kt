package com.example.kkocel.marvel.detail.presenter

import com.example.kkocel.marvel.detail.state.ComicDetailViewState
import com.example.kkocel.marvel.detail.view.DetailView
import com.example.kkocel.marvel.network.MarvelRepository
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DetailPresenter(val detailView: DetailView, val marvelRepository: MarvelRepository) {

    private var subscriptions = CompositeDisposable()

    fun onCreate(comicId: Int) {
        marvelRepository.getComicDetails(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            when (it) {
                                is ComicDetailViewState -> detailView.onDetailsLoaded(it)
                                else -> detailView.onNetworkError(it as ConnectionErrorViewState)
                            }
                        },
                        onError = { e -> detailView.onUnrecoverableError(e) }
                )
    }

    // TODO: Handle situations where internet is available but server is down
    fun retryWhenNetworkIsAvailable(comicId: Int) {
        subscriptions.add(ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { isConnectedToInternet ->
                            when {
                                isConnectedToInternet -> onCreate(comicId)
                            }
                        }))
    }

    fun forceReload(comicId: Int) = onCreate(comicId)

    fun onViewDestroyed() = subscriptions.dispose()
}
