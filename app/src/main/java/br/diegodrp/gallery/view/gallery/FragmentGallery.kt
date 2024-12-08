package br.diegodrp.gallery.view.gallery

import android.content.ContentUris
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.diegodrp.gallery.R
import br.diegodrp.gallery.databinding.FragmentGalleryBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.permission.OnPermissionRequestedCallback
import br.diegodrp.gallery.view.main.MainActivity
import br.diegodrp.gallery.viewmodel.gallery.GalleryEvent
import br.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentGallery : Fragment(R.layout.fragment_gallery), OnPermissionRequestedCallback {

    private lateinit var binding: FragmentGalleryBinding
    private val vm by viewModel<GalleryViewModel>()
    private lateinit var adapter: GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGalleryBinding.bind(view)

        askPermissions()

        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectState()
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = GalleryAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(GalleryItemDecoration(8))
    }

    private fun askPermissions() {
        val activity = requireActivity() as? MainActivity

        activity?.let {
            activity.registerOnPermissionRequested(this)
            if (activity.isStoragePermissionAllowed()) {
                vm.onEvent(GalleryEvent.OnPermissionsAllowed)
            } else {
                activity.requestStoragePermissions()
            }
        }
    }

    private fun collectState() {
        lifecycleScope.launch(Dispatchers.IO) {
            vm.state.collect { state ->
                loadImagesFromStorage(state.areAllPermissionsGranted)
                loadImagesToRecyclerView(state.images)
            }
        }
    }

    override fun onPermissionRequested(permission: String, isGranted: Boolean) {
        vm.onEvent(
            GalleryEvent.OnPermissionRequested(
                permission = permission,
                isGranted = isGranted
            )
        )
    }

    private suspend fun loadImagesToRecyclerView(images: List<Image>) {
        withContext(Dispatchers.Main) {
            adapter.submitList(images)
        }
    }

    private fun loadImagesFromStorage(areGranted: Boolean) {
        if (areGranted) {
            val images = loadImagesFromExternalStorage()
            vm.onEvent(GalleryEvent.OnGalleryImagesLoaded(images))
        }
    }

    private fun loadImagesFromExternalStorage(): List<Image> {
        return try {
            val imagesUri = if (Build.VERSION.SDK_INT >= 29) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.SIZE,
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            val images = mutableListOf<Image>()

            requireContext().contentResolver.query(
                imagesUri,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val displayColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
                val sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val size = cursor.getInt(sizeColumn)

                    val contentUri = ContentUris.withAppendedId(
                        imagesUri,
                        id
                    )

                    images.add(
                        Image(
                            id = id,
                            displayName = displayName,
                            width = width,
                            height = height,
                            size = size,
                            contentUri = contentUri
                        )
                    )
                }
            }
            images
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}