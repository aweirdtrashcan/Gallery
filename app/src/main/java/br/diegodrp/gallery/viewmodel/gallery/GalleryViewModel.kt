package br.diegodrp.gallery.viewmodel.gallery

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GalleryViewModel: ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state = _state.asStateFlow()

    private val permissionsMap = mutableMapOf<String, Boolean>()

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.OnPermissionRequested -> {
                permissionsMap[event.permission] = event.isGranted

                _state.value = _state.value.copy(
                    areAllPermissionsGranted = !permissionsMap.values.contains(false)
                )
            }

            GalleryEvent.OnPermissionsAllowed -> {
                _state.value = _state.value.copy(
                    areAllPermissionsGranted = true
                )
            }

            is GalleryEvent.OnGalleryImagesLoaded -> {
                _state.value = _state.value.copy(
                    images = event.images
                )
            }

            is GalleryEvent.OnAppBarCollapseChanged -> {
                if (_state.value.isAppBarCollapsed != event.collapsed) {
                    _state.value = _state.value.copy(
                        isAppBarCollapsed = event.collapsed
                    )
                }
            }
        }
    }
}