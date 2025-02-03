package com.diegodrp.gallery.viewmodel.album_grid

import com.diegodrp.gallery.model.Album

data class AlbumGridState(
    private val albums: List<Album> = emptyList()
)
