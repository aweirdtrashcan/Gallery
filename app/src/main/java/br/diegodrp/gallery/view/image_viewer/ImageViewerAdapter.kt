package br.diegodrp.gallery.view.image_viewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.diegodrp.gallery.databinding.ItemImageViewerBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.view.image_viewer.ImageViewerAdapter.ImageViewerViewHolder
import br.diegodrp.gallery.view.util.GalleryDiffUtil
import com.bumptech.glide.Glide

class ImageViewerAdapter:
    ListAdapter<Image, ImageViewerViewHolder>(GalleryDiffUtil()) {

    class ImageViewerViewHolder(
        val binding: ItemImageViewerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewerViewHolder {
        return ImageViewerViewHolder(
            ItemImageViewerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ImageViewerViewHolder,
        position: Int
    ) {
        val image = getItem(position)

        Glide.with(holder.binding.imageView)
            .load(image.contentUri)
            .into(holder.binding.imageView)
    }
}
