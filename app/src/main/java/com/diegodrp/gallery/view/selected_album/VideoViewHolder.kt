package com.diegodrp.gallery.view.selected_album

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.ResPreviewVideoBinding
import com.diegodrp.gallery.helpers.PreviewSizeCalculator
import com.diegodrp.gallery.model.Media
import com.diegodrp.gallery.model.Video

class VideoViewHolder(
    private val videoBinding: ResPreviewVideoBinding
) : ViewHolder(videoBinding.root) {
    private val glide = Glide.with(videoBinding.root.context)
    private val sizeCalculator = PreviewSizeCalculator()

    private val context = videoBinding.root.context
    private val size = sizeCalculator.calculatePreviewSize(
        context,
        context.resources.getInteger(R.integer.preview_size)
    )

    fun bind(video: Video, onMediaClicked: (media: Media) -> Unit) {
        videoBinding.root.layoutParams = videoBinding.root.layoutParams.apply {
            width = size.width
            height = size.height
        }

        glide
            .asBitmap()
            .load(video.uri)
            .override(size.width, size.height)
            .into(videoBinding.ivPreviewIconVideo)

        videoBinding.tvPreviewVideoLength.text = formatVideoDuration(video)
    }

    private fun formatVideoDuration(video: Video): String {
        val duration = video.duration / 1000

        return if (duration > 3600) {
            val minutes = (duration % 3600) / 60
            val seconds = duration % 60
            val hours = duration / 3600
            String.format("%d:%d:%02d", hours, minutes, seconds)
        } else {
            val minutes = duration / 60
            val seconds = duration % 60
            String.format("%d:%02d", minutes, seconds)
        }
    }
}