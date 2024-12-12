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
    private val onLongClick: (Image) -> Unit,
    private val onClick: (Image) -> Unit
) : ListAdapter<Image, GalleryAdapter.GalleryViewHolder>(GalleryDiffUtil()) {

    class GalleryViewHolder(
        private val imageBinding: ItemGalleryImageBinding
    ) : RecyclerView.ViewHolder(imageBinding.root) {

        private val glide = Glide.with(imageBinding.root.context)
        private val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .override(300, 300)

        fun bind(image: Image, onLongClick: (Image) -> Unit, onClick: (Image) -> Unit) {
            glide
                .load(image.contentUri)
                .apply(requestOptions)
                .into(imageBinding.imageView)

            imageBinding.imageView.setOnLongClickListener { onLongClick(image); true }
            imageBinding.imageView.setOnClickListener { onClick(image) }
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
        )
    }

    override fun onBindViewHolder(
        holder: GalleryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onLongClick, onClick)
    }
}