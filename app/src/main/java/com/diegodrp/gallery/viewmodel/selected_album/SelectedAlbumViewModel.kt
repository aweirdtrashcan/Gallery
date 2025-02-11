package com.diegodrp.gallery.viewmodel.selected_album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegodrp.gallery.data.MediaRepository
import com.diegodrp.gallery.helpers.Resource
import com.diegodrp.gallery.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedAlbumViewModel @Inject constructor(
    private val repository: MediaRepository
): ViewModel() {

    private val _state = MutableStateFlow(listOf<Media>())
    val state = _state.asStateFlow()

    fun getAlbum(albumName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val images = repository.loadImages()[albumName]
            val videos = repository.loadVideos()[albumName]

            val mediaList = mutableListOf<Media>()

            images?.forEach { mediaList.add(it) }
            videos?.forEach { mediaList.add(it) }

            mediaList.sortByDescending { it.date }

            _state.value = mediaList
        }
    }
}