package br.diegodrp.gallery.viewmodel.gallery

sealed class GalleryEvent {
    data class OnPermissionRequested(val permission: String, val isGranted: Boolean):
        GalleryEvent()
    data object OnPermissionsAllowed: GalleryEvent()
}