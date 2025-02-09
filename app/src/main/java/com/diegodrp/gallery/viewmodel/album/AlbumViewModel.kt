package com.diegodrp.gallery.viewmodel.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegodrp.gallery.data.MediaRepository
import com.diegodrp.gallery.helpers.Resource
import com.diegodrp.gallery.model.Album
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _albumState = MutableStateFlow(emptyList<Album>())
    val albumState = _albumState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorState = MutableSharedFlow<String>()
    val errorState = _errorState.asSharedFlow()

    fun onEvent(event: AlbumEvent) {
        when (event) {
            is AlbumEvent.LoadImages -> {
                processAlbums()
            }
        }
    }

    private fun processAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaRepository.loadAlbums().collect { albumResource ->
                when (albumResource) {
                    is Resource.Success -> {
                        _albumState.value = albumResource.data
                        albumResource.error?.let { _errorState.emit(it) }
                    }
                    is Resource.Error -> {
                        _errorState.emit(albumResource.error)
                        albumResource.data?.let { _albumState.value = it }
                    }
                    is Resource.Loading -> {
                        _loadingState.value = albumResource.isLoading
                        albumResource.data?.let { _albumState.value = it }
                    }
                }
            }
        }
    }

}