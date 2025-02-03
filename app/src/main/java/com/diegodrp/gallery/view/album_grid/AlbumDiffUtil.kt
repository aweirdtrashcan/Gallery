package com.diegodrp.gallery.view.album_grid

import androidx.recyclerview.widget.DiffUtil
import com.diegodrp.gallery.model.Album

class AlbumDiffUtil: DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }

}