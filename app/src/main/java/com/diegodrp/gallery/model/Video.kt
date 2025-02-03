package com.diegodrp.gallery.model

import android.net.Uri

data class Video(
    val id: Long,
    val name: String,
    val size: Int,
    val duration: Int,
    val date: Int,
    val uri: Uri
): Media
