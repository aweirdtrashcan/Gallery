package com.diegodrp.gallery.view.selected_album

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentPreviewGridBinding
import com.diegodrp.gallery.viewmodel.preview_grid.PreviewGridViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectedAlbumFragment: Fragment(R.layout.fragment_preview_grid) {

    private lateinit var binding: FragmentPreviewGridBinding
    private val vm by activityViewModels<PreviewGridViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPreviewGridBinding.bind(view)

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