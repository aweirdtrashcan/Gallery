package br.diegodrp.gallery.view.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.diegodrp.gallery.databinding.ItemImageBinding
import br.diegodrp.gallery.model.Image

class GalleryAdapter: ListAdapter<Image, GalleryAdapter.GalleryViewHolder>(GalleryDiffUtil()) {

    inner class GalleryViewHolder(
        val imageBinding: ItemImageBinding
    ): RecyclerView.ViewHolder(imageBinding.root)

    companion object {
        class GalleryDiffUtil: DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(
                oldItem: Image,
                newItem: Image
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Image,
                newItem: Image
            ): Boolean {
                return oldItem == newItem && oldItem.contentUri.compareTo(newItem.contentUri) == 0
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryViewHolder {
        return GalleryViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: GalleryViewHolder,
        position: Int
    ) {
        val image = getItem(position)

        holder.imageBinding.imageView.apply {
            setImageURI(image.contentUri)
        }
    }
}