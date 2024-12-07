package br.diegodrp.gallery.viewmodel.gallery

import br.diegodrp.gallery.model.Image

sealed class GalleryEvent {
    data class OnPermissionRequested(val permission: String, val isGranted: Boolean):
        GalleryEvent()
    data object OnPermissionsAllowed: GalleryEvent()
    data class OnGalleryImagesLoaded(val images: List<Image>): GalleryEvent()
}