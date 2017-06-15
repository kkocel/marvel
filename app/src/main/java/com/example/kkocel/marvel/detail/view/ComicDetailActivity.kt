package com.example.kkocel.marvel.detail.view

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.kkocel.marvel.R
import com.example.kkocel.marvel.detail.di.DetailModule
import com.example.kkocel.marvel.detail.presenter.DetailPresenter
import com.example.kkocel.marvel.detail.state.ComicDetailViewState
import com.example.kkocel.marvel.network.rest.RetrofitModule
import com.example.kkocel.marvel.state.ViewState
import kotlinx.android.synthetic.main.activity_comic_detail.*

class ComicDetailActivity : AppCompatActivity(), DetailView {

    companion object {
        val COMIC_ID = "COMIC_ID"
    }

    private val INVALID_COMIC_ID = -1

    private lateinit var detailPresenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedComic = intent.getIntExtra(COMIC_ID, INVALID_COMIC_ID)

        if (selectedComic == INVALID_COMIC_ID) {
            throw IllegalStateException("Can't run detail activity without ID")
        }

        val detailModule = DetailModule(RetrofitModule())

        setContentView(R.layout.activity_comic_detail)

        setSupportActionBar(toolbar)
        toolbar.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailPresenter = detailModule.provideListPresenter(this)
        detailPresenter.onCreate(selectedComic)
    }

    override fun onDetailsLoaded(detailViewState: ViewState) {
        if (detailViewState is ComicDetailViewState) {
            detail_comic_name.text = detailViewState.title
            detail_comic_overview.text = detailViewState.description

        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUnrecoverableError(e: Throwable) {
        // TODO: Send to crashlytics?
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, ListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
