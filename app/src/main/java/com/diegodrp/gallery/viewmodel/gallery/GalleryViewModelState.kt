package com.diegodrp.gallery.viewmodel.gallery

import com.diegodrp.gallery.model.Image

data class GalleryViewModelState(
    val images: List<Image> = listOf()
)
