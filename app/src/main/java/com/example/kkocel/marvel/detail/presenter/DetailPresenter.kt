package com.example.kkocel.marvel.detail.presenter

import com.example.kkocel.marvel.detail.view.DetailView
import com.example.kkocel.marvel.network.MarvelRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailPresenter(val detailView: DetailView, val marvelRepository: MarvelRepository) {

    private var subscriptions = CompositeDisposable()

    fun onCreate(comicId: Int) {
        marvelRepository.getComicDetails(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { detailViewState -> detailView.onDetailsLoaded(detailViewState) },
                        { e -> detailView.onUnrecoverableError(e) }
                )
    }

    fun forceReload(comicId: Int) = onCreate(comicId)

    fun onViewDestroyed() = subscriptions.dispose()
}
