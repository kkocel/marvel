package com.example.kkocel.marvel.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.kkocel.marvel.R
import kotlinx.android.synthetic.main.activity_comic_list.*

open class ListActivity : AppCompatActivity(), ListView {

    private lateinit var adapter: ComicRecyclerAdapter
    private lateinit var endlessScrollListener: ListEndlessScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //App.getApp().getComponent().plus(ListModule()).inject(this)

        setContentView(R.layout.activity_comic_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        setupRecyclerView()
        setupRefreshLayout()
        //setPresenterFactory { listPresenter }
        //endlessScrollListener.setListPresenter(listPresenter)

        if (savedInstanceState == null) {
            //getPresenter().startRequest()
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        comic_list.layoutManager = layoutManager
        comic_list.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))
        comic_list.itemAnimator = DefaultItemAnimator()
        adapter = ComicRecyclerAdapter()
        comic_list.adapter = adapter
        endlessScrollListener = ListEndlessScrollListener(layoutManager)
        comic_list.addOnScrollListener(endlessScrollListener)
    }

    private fun setupRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            //getPresenter().forceReload()
        }
    }

    private val spacing: Int
        get() = resources.getDimensionPixelSize(R.dimen.detail_margin)

    override fun onPageLoaded(page: ListPageViewState) {

        if (page is CorrectListPageViewState) {
            if (page.page == 1) {
                endlessScrollListener.resetState()
            }
            refreshLayout.isRefreshing = false
            adapter.swapList(page.comics)
        } else if (page is ErrorListViewState) {
            // TODO: Handle network/server error
        }
    }

    override fun onUnrecoverableError(throwable: Throwable) {
        // TODO: Send to crashlytics?
    }

    private class ListEndlessScrollListener internal constructor(layoutManager: GridLayoutManager) : EndlessScrollListener(layoutManager) {
        private lateinit var listPresenter: ListPresenter

        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            listPresenter.requestNextPage()
        }

        internal fun setListPresenter(listPresenter: ListPresenter) {
            this.listPresenter = listPresenter
        }
    }
}
