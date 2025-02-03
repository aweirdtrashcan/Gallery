package com.diegodrp.gallery.viewmodel.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegodrp.gallery.data.MediaRepository
import com.diegodrp.gallery.helpers.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: MediaRepository): ViewModel() {

    private val _state = MutableStateFlow(GalleryViewModelState())
    val state = _state.asStateFlow()

    fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadAlbums().collect { albums ->
                if (albums is Resource.Success) {
                    for (album in albums.data) {
                        println(album)
                    }
                }
            }
        }
    }

}