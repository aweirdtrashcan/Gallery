package com.diegodrp.gallery.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.diegodrp.gallery.R

class PreviewGridItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val padding = view.context.resources.getDimensionPixelSize(R.dimen.media_preview_padding)

        outRect.left = padding
        outRect.right = padding
        outRect.top = padding
        outRect.bottom = padding
    }
}