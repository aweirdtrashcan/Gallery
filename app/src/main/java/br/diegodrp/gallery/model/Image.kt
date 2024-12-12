package br.diegodrp.gallery.model

import android.graphics.Bitmap
import android.net.Uri

data class Image(
    val id: Long,
    val displayName: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val contentUri: Uri,
    val mime: String,
    val dateAdded: Long,
    val dateModified: Long
)
