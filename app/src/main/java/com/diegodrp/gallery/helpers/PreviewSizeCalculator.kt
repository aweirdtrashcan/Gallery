package com.diegodrp.gallery.helpers

import android.content.Context
import android.util.TypedValue
import kotlin.math.abs

class PreviewSizeCalculator {
    /* Calculates the size of the Preview based on screen width, the
       desired size of the preview and how many columns are being used
       in the StaggeredGridLayout */
    fun calculatePreviewSize(context: Context, previewSize: Int): Size {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val size = screenWidth / calculateColumnCount(context, previewSize)

        return Size(size, size)
    }

    /* Calculates how many columns there should be based on the screen width
       and the desired size of the preview.
     */
    fun calculateColumnCount(context: Context, previewSize: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        var columnCount = 1

        var sizeDifference = abs(screenWidth / columnCount) - previewSize

        while (sizeDifference > previewSize) {
            columnCount++
            sizeDifference = abs(screenWidth / columnCount) - previewSize
        }

        return columnCount
    }

    data class Size(val width: Int, val height: Int)
}