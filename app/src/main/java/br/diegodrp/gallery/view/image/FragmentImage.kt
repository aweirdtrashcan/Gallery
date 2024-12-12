package br.diegodrp.gallery.view.image

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.diegodrp.gallery.R
import br.diegodrp.gallery.databinding.FragmentImageBinding
import br.diegodrp.gallery.databinding.TabLayoutImageBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.viewmodel.gallery.GalleryState
import br.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentImage: Fragment(R.layout.fragment_image) {

    private lateinit var binding: FragmentImageBinding
    private val args by navArgs<FragmentImageArgs>()
    private val viewModel by inject<GalleryViewModel>()
    private lateinit var adapter: LargeImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageBinding.bind(view)

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
        if (!this::adapter.isInitialized) {
            adapter = LargeImageAdapter()
        }
        binding.viewPager.adapter = adapter

        val glide = Glide.with(requireContext())

        /*TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            *//*val image = viewModel.state.value.images[position]
            val tabLayoutImageView = TabLayoutImageBinding.inflate(layoutInflater)

            glide
                .load(image.contentUri)
                .override(50, 50)
                .into(tabLayoutImageView.tabLayoutImageView)

            tab.customView = tabLayoutImageView.root*//*
        }.attach()*/
    }

    private fun canImageBeLoaded(imageId: Int, images: List<Image>): Boolean {
        return imageId != -1 && images.find { it.id == imageId.toLong() } != null
    }

    private suspend fun collectState(state: GalleryState) {
        withContext(Dispatchers.Main) {
            if (!canImageBeLoaded(args.imageId, state.images)) {
                findNavController().popBackStack()
                return@withContext
            }

            adapter.submitList(state.images)

            val imageIndex = findImageIndex(args.imageId, state.images)
            jumpToCurrentImage(imageIndex)
        }
    }

    private fun jumpToCurrentImage(imageIndex: Int) {
        if (imageIndex == -1) {
            findNavController().popBackStack()
        } else {
            binding.viewPager.setCurrentItem(imageIndex, false)
        }
    }

    private fun findImageIndex(imageId: Int, images: List<Image>): Int {
        val image = images.find { it.id == imageId.toLong() }
        return images.indexOf(image)
    }
}