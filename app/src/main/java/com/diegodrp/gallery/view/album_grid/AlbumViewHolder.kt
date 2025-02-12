package com.diegodrp.gallery.view.album_grid

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.ResPreviewAlbumBinding
import com.diegodrp.gallery.helpers.PreviewSizeCalculator
import com.diegodrp.gallery.model.Album
import com.diegodrp.gallery.model.Image
import com.diegodrp.gallery.model.Media
import com.diegodrp.gallery.model.Video

// AlbumViewHolder is intended to be shared among multiple fragments.

class AlbumViewHolder(
    private val binding: ResPreviewAlbumBinding
) : ViewHolder(binding.root) {

    private val glide = Glide.with(binding.root)
    private val context = binding.root.context

    private val previewSize = context.resources.getInteger(R.integer.album_preview_size)
    private val size = PreviewSizeCalculator.calculatePreviewSize(
        context,
        previewSize
    )

    fun bind(album: Album, onAlbumClicked: (album: Album) -> Unit) {
        val medias = listOf(album.images.getOrNull(0), album.videos.getOrNull(0))
        var newestMedia: Media? = null

        for (media in medias) {
            if (newestMedia == null) {
                newestMedia = media
            } else if (media != null && media.date > newestMedia.date) {
                newestMedia = media
            }
        }

        binding.root.layoutParams = binding.root.layoutParams.apply {
            // Since the album view has to have space for the TextView
            // That will take ~19dp of the total space of the view.
            // I'm giving the view 19dp more size to account for that.
            val textViewHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                19f,
                context.resources.displayMetrics
            ).toInt()

            width = size.width
            height = size.width + textViewHeight
        }

        newestMedia?.let { media ->
            when (media) {
                is Image -> {
                    glide.load(media.uri).into(binding.ivPreviewIconImage)
                }

                is Video -> {
                    glide.load(media.uri).into(binding.ivPreviewIconImage)
                }

                else -> {}
            }
        }

        binding.albumName.text = album.name

        binding.root.setOnClickListener { onAlbumClicked(album) }
    }
}