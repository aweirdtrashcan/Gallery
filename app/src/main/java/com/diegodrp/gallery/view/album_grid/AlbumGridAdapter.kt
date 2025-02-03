package com.diegodrp.gallery.view.album_grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.diegodrp.gallery.databinding.ResPreviewAlbumBinding
import com.diegodrp.gallery.model.Album

class AlbumGridAdapter(
    private val onAlbumClicked: (album: Album) -> Unit
): ListAdapter<Album, AlbumViewHolder>(AlbumDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ResPreviewAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position), onAlbumClicked)
    }
}