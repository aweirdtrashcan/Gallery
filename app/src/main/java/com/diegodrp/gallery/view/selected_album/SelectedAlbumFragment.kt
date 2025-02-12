package com.diegodrp.gallery.view.selected_album

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentPreviewGridBinding
import com.diegodrp.gallery.helpers.PreviewGridItemDecoration
import com.diegodrp.gallery.helpers.PreviewSizeCalculator
import com.diegodrp.gallery.model.Image
import com.diegodrp.gallery.model.Media
import com.diegodrp.gallery.model.Video
import com.diegodrp.gallery.viewmodel.selected_album.SelectedAlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SelectedAlbumFragment : Fragment(R.layout.fragment_preview_grid) {

    private lateinit var binding: FragmentPreviewGridBinding
    private val selectedAlbumVm by viewModels<SelectedAlbumViewModel>()

    private val args by navArgs<SelectedAlbumFragmentArgs>()

    private val adapter by lazy {
        SelectedAlbumAdapter(this::onMediaClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPreviewGridBinding.bind(view)

        val previewSize = resources.getInteger(R.integer.media_preview_size)

        binding.rvGalleryPreview.adapter = adapter
        binding.rvGalleryPreview.layoutManager =
            StaggeredGridLayoutManager(
                PreviewSizeCalculator.calculateColumnCount(requireContext(), previewSize),
                RecyclerView.VERTICAL
            )
        binding.rvGalleryPreview.addItemDecoration(PreviewGridItemDecoration())


        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectedAlbumVm.state.collect { medias ->
                    collectMediaList(medias)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        selectedAlbumVm.getAlbum(args.albumName)
    }

    private suspend fun collectMediaList(medias: List<Media>) {
        withContext(Dispatchers.Main) {
            adapter.submitList(medias)
        }
    }

    private fun onMediaClicked(media: Media) {
        when (media) {
            is Image -> {
                loadImage(media)
            }
            is Video -> {
                loadVideo(media)
            }
        }
    }

    private fun loadImage(image: Image) {
        val loadImageNavigation = SelectedAlbumFragmentDirections.toImageFragment(
            image = image
        )
        findNavController().navigate(loadImageNavigation)
    }

    private fun loadVideo(video: Video) {
        val loadVideoNavigation = SelectedAlbumFragmentDirections.toVideoFragment(
            video = video
        )
        findNavController().navigate(loadVideoNavigation)
    }

}