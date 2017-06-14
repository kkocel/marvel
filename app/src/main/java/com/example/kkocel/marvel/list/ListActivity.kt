package com.example.kkocel.marvel.list

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.kkocel.marvel.R
import com.example.kkocel.marvel.list.di.ListModule
import com.example.kkocel.marvel.list.mvp.ListPresenter
import com.example.kkocel.marvel.list.mvp.ListView
import com.example.kkocel.marvel.network.rest.RetrofitModule
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comic_list.*


open class ListActivity : AppCompatActivity(), ListView {

    private lateinit var adapter: ComicRecyclerAdapter
    private lateinit var endlessScrollListener: ListEndlessScrollListener

    private lateinit var listPresenter: ListPresenter

    private var subscriptions = CompositeDisposable()

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
        list.layoutManager = layoutManager
        list.addItemDecoration(GridSpacingItemDecoration(2, resources.getDimensionPixelSize(R.dimen.detail_margin), true))
        list.itemAnimator = DefaultItemAnimator()
        adapter = ComicRecyclerAdapter()
        list.adapter = adapter
        endlessScrollListener = ListEndlessScrollListener(layoutManager)
        list.addOnScrollListener(endlessScrollListener)
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            listPresenter.forceReload()
        }
    }

    override fun onPageLoaded(page: ListPageViewState) {
        if (page is CorrectListPageViewState) {
            if (page.offset == 0) {
                endlessScrollListener.resetState()
            }
            refreshLayout.isRefreshing = false
            adapter.swapList(page.comics)
        } else if (page is ConnectionErrorListViewState) {
            displaySnackbar(getString(R.string.no_network))

            retryWhenNetworkIsAvailable()
        }
    }

    private fun retryWhenNetworkIsAvailable() {
        subscriptions.add(ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isConnectedToInternet ->
                    if (isConnectedToInternet) {
                        listPresenter.retryCurrentPage(endlessScrollListener.currentPage * 100)
                        dispose()
                    }
                }))
    }

    private fun dispose() {
        subscriptions.dispose()
        subscriptions = CompositeDisposable()
    }

    override fun onUnrecoverableError(throwable: Throwable) {
        displaySnackbar(getString(R.string.serious_problem))
    }

    fun displaySnackbar(message: String) = Snackbar.make(findViewById(R.id.list_root)!!, message,
            Snackbar.LENGTH_LONG).show()

    private class ListEndlessScrollListener internal constructor(layoutManager: GridLayoutManager) : EndlessScrollListener(layoutManager) {
        private lateinit var listPresenter: ListPresenter

        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            listPresenter.requestNextPage(page * 100)
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
}
