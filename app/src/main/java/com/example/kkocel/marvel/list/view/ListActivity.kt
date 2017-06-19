package com.example.kkocel.marvel.list.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.kkocel.marvel.R
import com.example.kkocel.marvel.list.di.ListModule
import com.example.kkocel.marvel.list.presenter.ListPresenter
import com.example.kkocel.marvel.list.presenter.ListPresenter.Companion.pageToOffset
import com.example.kkocel.marvel.list.state.CorrectListPageViewState
import com.example.kkocel.marvel.network.rest.RetrofitModule
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import com.example.kkocel.marvel.view.displaySnackbar
import kotlinx.android.synthetic.main.activity_comic_list.*

open class ListActivity : AppCompatActivity(), ListView {

    private lateinit var adapter: ComicRecyclerAdapter
    private lateinit var endlessScrollListener: ListEndlessScrollListener

    private lateinit var listPresenter: ListPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_list)

        val listModule = ListModule(RetrofitModule())
        listPresenter = listModule.provideListPresenter(this)
        listPresenter.onCreate()

        setupToolbar()
        setupRecyclerView()
        setupRefreshLayout()
        endlessScrollListener.setListPresenter(listPresenter)

    }

    private fun setupToolbar() {
        setSupportActionBar(tlbListToolbar)
        tlbListToolbar.title = title
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        adapter = ComicRecyclerAdapter()
        rclListRecycler.layoutManager = layoutManager
        rclListRecycler.addItemDecoration(GridSpacingItemDecoration(2, resources.getDimensionPixelSize(R.dimen.detail_margin), true))
        rclListRecycler.itemAnimator = DefaultItemAnimator()
        rclListRecycler.adapter = adapter
        endlessScrollListener = ListEndlessScrollListener(layoutManager)
        rclListRecycler.addOnScrollListener(endlessScrollListener)
    }

    private fun setupRefreshLayout() {
        swpListRefreshLayout.setOnRefreshListener {
            listPresenter.forceReload()
        }
        swpListRefreshLayout.isRefreshing = true
    }

    override fun onPageLoaded(page: CorrectListPageViewState) {
        if (page.offset == 0) {
            endlessScrollListener.resetState()
        }
        swpListRefreshLayout.isRefreshing = false
        adapter.swapList(page.comics)
    }

    override fun onNetworkError(it: ConnectionErrorViewState) {
        displaySnackbar(getString(R.string.noNetwork))
        listPresenter.retryWhenNetworkIsAvailable(endlessScrollListener.currentPage)
    }

    override fun onUnrecoverableError(throwable: Throwable) {
        displaySnackbar(getString(R.string.seriousProblem))
        // TODO: Log to Crashlytics?
    }

    private class ListEndlessScrollListener(layoutManager: GridLayoutManager) : EndlessScrollListener(layoutManager) {
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
    }
}
