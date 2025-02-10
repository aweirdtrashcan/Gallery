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
    private val size = sizeCalculator.calculatePreviewSize(context)

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

        videoBinding.tvPreviewVideoLength.text = calculateVideoLength(video)
    }

    private fun calculateVideoLength(video: Video): String {
        val length = video.duration / 61235f
        return length.toString().replace(".", ":")
    }
}