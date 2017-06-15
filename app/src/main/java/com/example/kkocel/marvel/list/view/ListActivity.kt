package com.example.kkocel.marvel.list.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.kkocel.marvel.R
import com.example.kkocel.marvel.network.rest.RetrofitModule
import com.example.kkocel.marvel.list.di.ListModule
import com.example.kkocel.marvel.list.presenter.ListPresenter
import com.example.kkocel.marvel.list.state.CorrectListPageViewState
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import com.example.kkocel.marvel.state.ViewState
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comic_list.*

open class ListActivity : AppCompatActivity(), ListView {

    private lateinit var adapter: ComicRecyclerAdapter
    private lateinit var endlessScrollListener: ListEndlessScrollListener

    private lateinit var listPresenter: ListPresenter

    private var subscriptions = io.reactivex.disposables.CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_list)

        val listModule = ListModule(RetrofitModule())
        listPresenter = listModule.provideListPresenter(this)

        listPresenter.onCreate()

        setSupportActionBar(toolbar)
        toolbar.title = title

        setupRecyclerView()
        setupRefreshLayout()
        endlessScrollListener.setListPresenter(listPresenter)

    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        adapter = ComicRecyclerAdapter()
        list.layoutManager = layoutManager
        list.addItemDecoration(GridSpacingItemDecoration(2, resources.getDimensionPixelSize(R.dimen.detail_margin), true))
        list.itemAnimator = DefaultItemAnimator()
        list.adapter = adapter
        endlessScrollListener = ListEndlessScrollListener(layoutManager)
        list.addOnScrollListener(endlessScrollListener)
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            listPresenter.forceReload()
        }
        refreshLayout.isRefreshing = true
    }

    override fun onPageLoaded(page: ViewState) {
        if (page is CorrectListPageViewState) {
            if (page.offset == 0) {
                endlessScrollListener.resetState()
            }
            refreshLayout.isRefreshing = false
            adapter.swapList(page.comics)
        } else if (page is ConnectionErrorViewState) {
            displaySnackbar(getString(R.string.no_network))
            retryWhenNetworkIsAvailable()
        }
    }

    // TODO: Handle situations where internet is available but server is down
    private fun retryWhenNetworkIsAvailable() {
        subscriptions.add(ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isConnectedToInternet ->
                    if (isConnectedToInternet) {
                        listPresenter.retryCurrentPage(pageToOffset(endlessScrollListener.currentPage))
                        dispose()
                    }
                }))
    }

    private fun dispose() {
        subscriptions.dispose()
        subscriptions = CompositeDisposable()
    }

    override fun onUnrecoverableError(throwable: Throwable) {
        displaySnackbar(getString(com.example.kkocel.marvel.R.string.serious_problem))
    }

    fun displaySnackbar(message: String) = Snackbar.make(findViewById(R.id.list_root)!!, message, Snackbar.LENGTH_LONG).show()


    private class ListEndlessScrollListener internal constructor(layoutManager: GridLayoutManager) : EndlessScrollListener(layoutManager) {
        private lateinit var listPresenter: ListPresenter

        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            listPresenter.requestNextPage(pageToOffset(page))
        }

        internal fun setListPresenter(listPresenter: ListPresenter) {
            this.listPresenter = listPresenter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listPresenter.onViewDestroyed()
        subscriptions.dispose()
    }

    companion object {
        fun pageToOffset(page: Int) = page * 100
    }
}
