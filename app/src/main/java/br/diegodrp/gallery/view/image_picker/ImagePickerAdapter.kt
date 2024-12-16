package br.diegodrp.gallery.view.image_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import br.diegodrp.gallery.databinding.ItemImagePickerBinding
import br.diegodrp.gallery.model.Image

class ImagePickerAdapter(
    private val onImagePickerClicked: (imagePosition: Int) -> Unit
): ListAdapter<Image, ImagePickerViewHolder>(ImagePickerDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagePickerViewHolder {
        return ImagePickerViewHolder(
            ItemImagePickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ImagePickerViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onImagePickerClicked, position)
    }
}