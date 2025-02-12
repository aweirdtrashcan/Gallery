package com.diegodrp.gallery.view.selected_album

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.ResPreviewImageBinding
import com.diegodrp.gallery.helpers.PreviewSizeCalculator
import com.diegodrp.gallery.model.Image
import com.diegodrp.gallery.model.Media

class ImageViewHolder(
    private val imageBinding: ResPreviewImageBinding
): ViewHolder(imageBinding.root) {

    private val glide = Glide.with(imageBinding.root.context)

    private val context = imageBinding.ivPreviewIconImage.context
    private val size = PreviewSizeCalculator.calculatePreviewSize(
        context,
        context.resources.getInteger(R.integer.media_preview_size)
    )

    fun bind(image: Image, onMediaClicked: (media: Media) -> Unit) {
        imageBinding.root.layoutParams = imageBinding.root.layoutParams.apply {
            width = size.width
            height = size.height
        }

        glide
            .load(image.uri)
            // TODO: dynamic size for image.
            .override(size.width, size.height)
            .into(imageBinding.ivPreviewIconImage)

        imageBinding.root.setOnClickListener { onMediaClicked(image) }
    }
}