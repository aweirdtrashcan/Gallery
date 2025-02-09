package com.diegodrp.gallery.viewmodel.selected_album

import com.diegodrp.gallery.model.Image

data class SelectedAlbumState(
    val images: List<Image> = listOf()
)
