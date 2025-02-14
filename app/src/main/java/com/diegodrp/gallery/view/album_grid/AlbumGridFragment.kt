package com.diegodrp.gallery.view.album_grid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentAlbumGridBinding
import com.diegodrp.gallery.extensions.hasPermission
import com.diegodrp.gallery.helpers.Permission
import com.diegodrp.gallery.helpers.PreviewGridItemDecoration
import com.diegodrp.gallery.helpers.PreviewSizeCalculator
import com.diegodrp.gallery.helpers.ReadImages
import com.diegodrp.gallery.helpers.ReadVideos
import com.diegodrp.gallery.helpers.isTiramisuPlus
import com.diegodrp.gallery.model.Album
import com.diegodrp.gallery.view.permission.PermissionActivity
import com.diegodrp.gallery.view.permission.showPermissionDialog
import com.diegodrp.gallery.viewmodel.album.AlbumEvent
import com.diegodrp.gallery.viewmodel.album.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AlbumGridFragment : Fragment(R.layout.fragment_album_grid) {

    private lateinit var binding: FragmentAlbumGridBinding
    private val vm by activityViewModels<AlbumViewModel>()

    private val adapter by lazy {
        AlbumGridAdapter(this::onAlbumClicked)
    }

    private var hasReadImages = false
    private var hasReadVideos = false

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

        handlePermissions()

        val previewSize = resources.getInteger(R.integer.album_preview_size)

        binding.rvAlbums.adapter = adapter
        binding.rvAlbums.layoutManager = StaggeredGridLayoutManager(
            PreviewSizeCalculator.calculateColumnCount(requireContext(), previewSize),
            RecyclerView.VERTICAL
        )
        binding.rvAlbums.addItemDecoration(PreviewGridItemDecoration())
    }

    private fun handlePermissions() {
        val permissionToRequest = mutableListOf<Permission>()

        (activity as PermissionActivity?)?.let { activity ->
            with(activity) {
                if (isTiramisuPlus()) {
                    if (!hasPermission(ReadImages)) {
                        permissionToRequest.add(ReadImages)
                    } else {
                        hasReadImages = true
                    }

                    if (!hasPermission(ReadVideos)) {
                        permissionToRequest.add(ReadVideos)
                    } else {
                        hasReadVideos = true
                    }
                } else {
                    /* TODO: Old Android permissions */
                }

                activity.handlePermissions(
                    permissionToRequest,
                    this@AlbumGridFragment::permissionsResultCallback
                )

                checkPermissionsAreGranted()
            }
        }
    }

    private fun permissionsResultCallback(permission: Permission, isGranted: Boolean) {
        when (permission) {
            is ReadVideos -> hasReadVideos = isGranted
            is ReadImages -> hasReadImages = isGranted
        }
        checkPermissionsAreGranted()
    }

    private fun checkPermissionsAreGranted() {
        if (hasReadImages && hasReadVideos) {
            vm.onEvent(AlbumEvent.LoadImages)
        }
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