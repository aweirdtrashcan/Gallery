package br.diegodrp.gallery.view.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.diegodrp.gallery.databinding.ItemGalleryImageBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.view.util.GalleryDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class GalleryAdapter(
    private val onLongClick: (imagePosition: Int) -> Unit,
    private val onClick: (imagePosition: Int) -> Unit
) : ListAdapter<Image, GalleryAdapter.GalleryViewHolder>(GalleryDiffUtil()) {

    class GalleryViewHolder(
        private val binding: ItemGalleryImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val glide = Glide.with(binding.root.context)
        private val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .override(300, 300)

        fun bind(image: Image) {
            glide
                .load(image.contentUri)
                .apply(requestOptions)
                .into(binding.imageView)
        }

        fun setOnClickListeners(
            onLongClick: (imagePosition: Int) -> Unit,
            onClick: (imagePosition: Int) -> Unit) {
            binding.imageView.setOnClickListener { onClick(bindingAdapterPosition) }
            binding.imageView.setOnLongClickListener { onLongClick(bindingAdapterPosition); true }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryViewHolder {
        return GalleryViewHolder(
            ItemGalleryImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply { setOnClickListeners(onLongClick, onClick) }
    }

    override fun onBindViewHolder(
        holder: GalleryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}