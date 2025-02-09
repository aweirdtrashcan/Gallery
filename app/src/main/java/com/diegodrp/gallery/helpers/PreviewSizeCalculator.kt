package com.diegodrp.gallery.helpers

import android.content.Context
import android.util.TypedValue

class PreviewSizeCalculator {
    fun calculatePreviewSize(context: Context): Size {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val columnCount = 3

        val spacingPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 0f, displayMetrics
        )

        val previewSize = screenWidth / columnCount

        return Size(previewSize, previewSize)
    }

    data class Size(val width: Int, val height: Int)
}