package com.example.kkocel.marvel.detail.view

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import com.bumptech.glide.Glide
import com.example.kkocel.marvel.R
import com.example.kkocel.marvel.detail.di.DetailModule
import com.example.kkocel.marvel.detail.presenter.DetailPresenter
import com.example.kkocel.marvel.detail.state.ComicDetailViewState
import com.example.kkocel.marvel.network.rest.RetrofitModule
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import com.example.kkocel.marvel.view.ThumbnailUrlLoader
import com.example.kkocel.marvel.view.displaySnackbar
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_comic_detail.*


class ComicDetailActivity : AppCompatActivity(), DetailView {

    companion object {
        val COMIC_ID = "COMIC_ID"
    }

    private val INVALID_COMIC_ID = -1

    private lateinit var detailPresenter: DetailPresenter
    private var selectedComic: Int = INVALID_COMIC_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedComic = intent.getIntExtra(COMIC_ID, INVALID_COMIC_ID)

        if (selectedComic == INVALID_COMIC_ID) {
            throw IllegalStateException("Can't run detail activity without ID")
        }

        val detailModule = DetailModule(RetrofitModule())

        setContentView(R.layout.activity_comic_detail)
        setupToolbar()

        detailPresenter = detailModule.provideListPresenter(this)
        detailPresenter.onCreate(selectedComic)

        swpDetailRefreshLayout.isRefreshing = true
        swpDetailRefreshLayout.setOnRefreshListener {
            detailPresenter.forceReload(selectedComic)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(tlbDetailToolbar)
        tlbDetailToolbar.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun fadeIn(view: View) {
        val animate = AlphaAnimation(0f, 1f)
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    override fun onDetailsLoaded(detailViewState: ComicDetailViewState) {
        crdDetailContent.visibility = View.VISIBLE
        fadeIn(crdDetailContent)
        swpDetailRefreshLayout.isRefreshing = false
        txtDetailComicName.text = detailViewState.title
        txtDetailComicOverview.text = detailViewState.description
        Glide.with(this).using(ThumbnailUrlLoader(this)).load(detailViewState.thumbnail).into(imgDetailThumbnail)
        Glide.with(this).using(BackdropDetailUrlLoader(this)).load(detailViewState.image)
                .bitmapTransform(BlurTransformation(this, 10))
                .into(imgDetailBackdrop)
    }

    override fun onNetworkError(detailViewState: ConnectionErrorViewState) {
        displaySnackbar(getString(R.string.noNetwork))
        detailPresenter.retryWhenNetworkIsAvailable(selectedComic)
    }

    override fun onUnrecoverableError(e: Throwable) {
        displaySnackbar(getString(R.string.seriousProblem))
        // TODO: Log to Crashlytics?
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, ListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPresenter.onViewDestroyed()
    }
}
