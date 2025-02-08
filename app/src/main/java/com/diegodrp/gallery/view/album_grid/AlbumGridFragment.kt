package com.diegodrp.gallery.view.album_grid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentAlbumGridBinding
import com.diegodrp.gallery.model.Album
import com.diegodrp.gallery.viewmodel.album_grid.AlbumGridEvent
import com.diegodrp.gallery.viewmodel.album_grid.AlbumGridViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AlbumGridFragment: Fragment(R.layout.fragment_album_grid) {

    private lateinit var binding: FragmentAlbumGridBinding
    private lateinit var adapter: AlbumGridAdapter
    private val vm by viewModels<AlbumGridViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlbumGridBinding.bind(view)

        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.albumState.collect {
                    collectAlbumState(it)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.errorState.collect {
                    collectErrorState(it)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.loadingState.collect {
                    collectLoadingState(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        adapter = AlbumGridAdapter(this::onAlbumClicked)
        binding.rvAlbums.adapter = adapter
        binding.rvAlbums.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        vm.onEvent(AlbumGridEvent.LoadImages)
    }

    private fun onAlbumClicked(album: Album) {
        val albumPreviewArgs = AlbumGridFragmentDirections.loadAlbum(album.name)
        findNavController().navigate(albumPreviewArgs)
    }

    private suspend fun collectAlbumState(albums: List<Album>) {
        withContext(Dispatchers.Main) {
            adapter.submitList(albums)
        }
    }

    private suspend fun collectErrorState(error: String?) {
        // TODO: Handle error
    }

    private suspend fun collectLoadingState(isLoading: Boolean) {
        // TODO: Show that it's loading
    }
}