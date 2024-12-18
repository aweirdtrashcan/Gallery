package br.diegodrp.gallery.view.image_viewer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.diegodrp.gallery.R
import br.diegodrp.gallery.databinding.FragmentImageViewerBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.view.image_picker.ImagePickerAdapter
import br.diegodrp.gallery.viewmodel.gallery.GalleryState
import br.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import org.koin.android.ext.android.inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentImageViewer: Fragment(R.layout.fragment_image_viewer) {

    private lateinit var binding: FragmentImageViewerBinding
    private val args by navArgs<FragmentImageViewerArgs>()
    private val viewModel by inject<GalleryViewModel>()
    private val imageViewerAdapter = ImageViewerAdapter()
    private val imagePickerAdapter = ImagePickerAdapter(this::onImagePickerClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageViewerBinding.bind(view)

        setupRecyclerView()

        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    collectState(it)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.viewPager.adapter = imageViewerAdapter
        binding.viewPager.offscreenPageLimit = 3

        binding.imagePickerRecyclerView.adapter = imagePickerAdapter
        binding.imagePickerRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
    }

    private suspend fun collectState(state: GalleryState) {
        withContext(Dispatchers.Main) {
            val isPositionInvalid = args.imagePosition == -1
            if (isPositionInvalid) {
                findNavController().popBackStack()
                return@withContext
            }

            imageViewerAdapter.submitList(state.images)
            imagePickerAdapter.submitList(state.images)

            binding.viewPager.setCurrentItem(args.imagePosition, false)
        }
    }

    private fun onImagePickerClicked(imagePosition: Int) {
        binding.viewPager.setCurrentItem(imagePosition, true)
        binding.imagePickerRecyclerView.smoothScrollToPosition(imagePosition)
    }
}