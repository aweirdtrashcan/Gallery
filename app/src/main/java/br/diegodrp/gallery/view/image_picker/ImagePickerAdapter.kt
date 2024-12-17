package br.diegodrp.gallery.view.image_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.diegodrp.gallery.databinding.ItemImagePickerBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.view.image_picker.ImagePickerAdapter.ImagePickerViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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
        ).apply { setOnClickListeners { onImagePickerClicked(it) } }
    }

    override fun onBindViewHolder(
        holder: ImagePickerViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ImagePickerViewHolder(
        private val binding: ItemImagePickerBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            Glide.with(binding.imagePickerImageView.context)
                .load(image.contentUri)
                .apply(RequestOptions().centerCrop())
                .into(binding.imagePickerImageView)
        }

        fun setOnClickListeners(onImagePickerClicked: (imagePosition: Int) -> Unit) {
            binding.imagePickerImageView.setOnClickListener {
                onImagePickerClicked(bindingAdapterPosition)
            }
        }
    }
}