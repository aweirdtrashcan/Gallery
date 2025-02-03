package com.diegodrp.gallery.viewmodel.album_grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegodrp.gallery.data.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumGridViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumGridState())
    val state = _state.asStateFlow()

    fun onEvent(event: AlbumGridEvent) {
        when (event) {
            is AlbumGridEvent.LoadImages -> {
                mediaRepository.loadAlbums()
            }
        }
    }

    private fun processAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaRepository.loadAlbums().collect { albumResource ->
                when (albumResource) {

                }
            }
        }
    }

}