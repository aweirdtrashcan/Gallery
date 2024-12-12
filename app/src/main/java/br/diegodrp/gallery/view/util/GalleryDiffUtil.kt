package br.diegodrp.gallery.view.util

import androidx.recyclerview.widget.DiffUtil
import br.diegodrp.gallery.model.Image

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