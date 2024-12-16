package br.diegodrp.gallery.view.image_picker

import androidx.recyclerview.widget.RecyclerView
import br.diegodrp.gallery.databinding.ItemImagePickerBinding
import br.diegodrp.gallery.model.Image
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImagePickerViewHolder(
    private val binding: ItemImagePickerBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image, onImagePickerClicked: (Int) -> Unit, imagePosition: Int) {
        binding.imagePickerImageView.setOnClickListener {
            onImagePickerClicked(imagePosition)
        }

        Glide.with(binding.imagePickerImageView.context)
            .load(image.contentUri)
            .apply(RequestOptions().centerCrop())
            .into(binding.imagePickerImageView)
    }
}