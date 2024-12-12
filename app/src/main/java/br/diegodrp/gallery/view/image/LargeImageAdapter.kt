package br.diegodrp.gallery.view.image

import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.diegodrp.gallery.databinding.ItemLargeImageBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.view.image.LargeImageAdapter.LargeImageViewHolder
import br.diegodrp.gallery.view.util.GalleryDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

data class ImageSize(val width: Int, val height: Int)

class LargeImageAdapter : ListAdapter<Image, LargeImageViewHolder>(GalleryDiffUtil()) {

    class LargeImageViewHolder(
        val binding: ItemLargeImageBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LargeImageViewHolder {
        return LargeImageViewHolder(
            ItemLargeImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: LargeImageViewHolder,
        position: Int
    ) {
        val image = getItem(position)

        Glide.with(holder.binding.imageView)
            .load(image.contentUri)
            .into(holder.binding.imageView)
    }

}
