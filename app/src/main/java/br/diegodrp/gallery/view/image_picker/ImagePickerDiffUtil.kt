package br.diegodrp.gallery.view.image_picker

import androidx.recyclerview.widget.DiffUtil
import br.diegodrp.gallery.model.Image

class ImagePickerDiffUtil: DiffUtil.ItemCallback<Image>() {
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
        return oldItem == newItem
    }

}