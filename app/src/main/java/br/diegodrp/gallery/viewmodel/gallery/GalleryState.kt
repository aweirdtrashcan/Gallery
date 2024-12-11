package br.diegodrp.gallery.viewmodel.gallery

import br.diegodrp.gallery.model.Image

data class GalleryState(
    val areAllPermissionsGranted: Boolean = false,
    val images: List<Image> = listOf(),
    val isAppBarCollapsed: Boolean = false
)