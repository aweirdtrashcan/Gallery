package com.diegodrp.gallery.viewmodel.album

sealed class AlbumEvent {
    data object LoadImages: AlbumEvent()
}