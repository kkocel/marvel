package com.example.kkocel.marvel.list.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : android.support.v7.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: android.graphics.Rect, view: android.view.View, parent: android.support.v7.widget.RecyclerView, state: android.support.v7.widget.RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}
