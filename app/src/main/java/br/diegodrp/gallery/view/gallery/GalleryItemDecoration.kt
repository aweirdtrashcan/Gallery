package br.diegodrp.gallery.view.gallery

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GalleryItemDecoration(
    private val padding: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.left = padding
        outRect.right = padding
        outRect.top = padding
        outRect.bottom = padding
    }
}