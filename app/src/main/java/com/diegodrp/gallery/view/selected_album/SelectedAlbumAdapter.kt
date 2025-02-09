package com.diegodrp.gallery.view.selected_album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.diegodrp.gallery.databinding.ResPreviewImageBinding
import com.diegodrp.gallery.databinding.ResPreviewVideoBinding
import com.diegodrp.gallery.model.Image
import com.diegodrp.gallery.model.Media
import com.diegodrp.gallery.model.Video

class SelectedAlbumAdapter(
    private val onMediaClicked: (media: Media) -> Unit
) : ListAdapter<Media, ViewHolder>(MediaDiffUtil()) {

    companion object {
        class MediaDiffUtil : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                if (oldItem is Image && newItem is Image) {
                    return oldItem.id == newItem.id
                } else if (oldItem is Video && newItem is Video) {
                    return oldItem.id == newItem.id
                }
                return false
            }

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
                if (oldItem is Image && newItem is Image) {
                    return oldItem == newItem
                } else if (oldItem is Video && newItem is Video) {
                    return oldItem == newItem
                }
                return false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> {
                ImageViewHolder(
                    ResPreviewImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            1 -> {
                VideoViewHolder(
                    ResPreviewVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                throw IllegalStateException("Media has to be either an Image or a Video")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is Image -> 0
            is Video -> 1
            else -> {
                throw IllegalStateException("Media has to be either an Image or a Video")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item is Image) {
            (holder as ImageViewHolder).bind(item, onMediaClicked)
        } else if (item is Video) {
            (holder as VideoViewHolder).bind(item, onMediaClicked)
        }
    }

}