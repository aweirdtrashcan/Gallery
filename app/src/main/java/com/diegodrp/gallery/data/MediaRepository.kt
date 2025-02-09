package com.diegodrp.gallery.data

import com.diegodrp.gallery.helpers.Resource
import com.diegodrp.gallery.model.Album
import com.diegodrp.gallery.model.Image
import com.diegodrp.gallery.model.Video
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun loadImages(): Map<String, List<Image>>
    fun loadVideos(): Map<String, List<Video>>
    fun loadAlbums(): Flow<Resource<List<Album>>>
}