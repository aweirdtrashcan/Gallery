package com.diegodrp.gallery.viewmodel.album_grid

sealed class AlbumGridEvent {
    data object LoadImages: AlbumGridEvent()
}