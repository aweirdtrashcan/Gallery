package com.diegodrp.gallery.model

import android.net.Uri

data class Image(
    val id: Long,
    val name: String,
    val size: Int,
    val date: Int,
    val uri: Uri
): Media