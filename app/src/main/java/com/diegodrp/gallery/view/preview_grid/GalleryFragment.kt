package com.diegodrp.gallery.view.preview_grid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentGalleryBinding
import com.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class GalleryFragment: Fragment(R.layout.fragment_gallery) {

    private lateinit var binding: FragmentGalleryBinding
    private val vm by activityViewModels<GalleryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGalleryBinding.bind(view)

        /*binding.rvGalleryPreview.adapter = adapter
        binding.rvGalleryPreview.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        */

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                /*withContext(Dispatchers.Main) {
                    adapter.submitList(vm.state.value.images)
                }*/
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vm.getImages()
    }

}