package com.diegodrp.gallery.view.selected_album

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentPreviewGridBinding
import com.diegodrp.gallery.helpers.PreviewGridItemDecoration
import com.diegodrp.gallery.helpers.PreviewSizeCalculator
import com.diegodrp.gallery.model.Media
import com.diegodrp.gallery.viewmodel.album.AlbumViewModel
import com.diegodrp.gallery.viewmodel.selected_album.SelectedAlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectedAlbumFragment : Fragment(R.layout.fragment_preview_grid) {

    private lateinit var binding: FragmentPreviewGridBinding
    private val selectedAlbumVm by viewModels<SelectedAlbumViewModel>()
    private val albumVm by activityViewModels<AlbumViewModel>()

    private val args by navArgs<SelectedAlbumFragmentArgs>()

    private val adapter by lazy {
        SelectedAlbumAdapter(this::onMediaClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPreviewGridBinding.bind(view)

        val sizeCalculator = PreviewSizeCalculator()
        val previewSize = resources.getInteger(R.integer.preview_size)

        binding.rvGalleryPreview.adapter = adapter
        binding.rvGalleryPreview.layoutManager =
            StaggeredGridLayoutManager(
                sizeCalculator.calculateColumnCount(requireContext(), previewSize),
                RecyclerView.VERTICAL
            )
        binding.rvGalleryPreview.addItemDecoration(PreviewGridItemDecoration())
    }

    override fun onStart() {
        super.onStart()

        val album = albumVm.albumState.value.find { it.name == args.albumName }

        if (album == null) {
            Toast.makeText(
                requireContext(),
                "Error loading album ${args.albumName}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        } else {
            val mediaList = mutableListOf<Media>()

            album.images.forEach { mediaList.add(it) }
            album.videos.forEach { mediaList.add(it) }

            mediaList.sortByDescending { it.date }

            adapter.submitList(mediaList)
        }
    }

    private fun onMediaClicked(media: Media) {

    }

}