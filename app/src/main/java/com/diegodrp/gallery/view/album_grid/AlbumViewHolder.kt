package com.diegodrp.gallery.view.album_grid

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.diegodrp.gallery.databinding.ResPreviewAlbumBinding
import com.diegodrp.gallery.model.Album
import com.diegodrp.gallery.model.Image

class AlbumViewHolder(
    private val binding: ResPreviewAlbumBinding
): ViewHolder(binding.root) {

    private val glide = Glide.with(binding.root)

    fun bind(album: Album, onAlbumClicked: (album: Album) -> Unit) {
        var lastBiggestDate = 0
        var newestImage: Image? = null
        album.images.forEach { image ->
            if (image.date > lastBiggestDate) {
                newestImage = image
                lastBiggestDate = image.date
            }
        }

        binding.albumName.text = album.name
        newestImage?.let { image ->
            glide.load(image.uri).into(binding.ivPreviewIconImage)
        }

        binding.root.setOnClickListener { onAlbumClicked(album) }
    }
}