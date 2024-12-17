package br.diegodrp.gallery.view.gallery

import android.content.ContentUris
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.diegodrp.gallery.R
import br.diegodrp.gallery.databinding.BottomSheetImageInfoBinding
import br.diegodrp.gallery.databinding.FragmentGalleryBinding
import br.diegodrp.gallery.model.Image
import br.diegodrp.gallery.permission.OnPermissionRequestedCallback
import br.diegodrp.gallery.view.main.MainActivity
import br.diegodrp.gallery.viewmodel.gallery.GalleryEvent
import br.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class FragmentGallery : Fragment(R.layout.fragment_gallery), OnPermissionRequestedCallback {

    private lateinit var binding: FragmentGalleryBinding
    private val vm by inject<GalleryViewModel>()
    private lateinit var adapter: GalleryAdapter
    private var recyclerViewState: Parcelable? = null

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
        listenToolbarCollapseState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewState != null) {
            binding.recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onPause() {
        super.onPause()
        recyclerViewState = binding.recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroy() {
        super.onDestroy()
        val activity = requireActivity() as? MainActivity
        activity?.unregisterOnPermissionRequested(this)
    }

    private fun listenToolbarCollapseState() {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            appBarLayout?.let {
                appBarLayout
                val isCollapsed = appBarLayout.totalScrollRange == -verticalOffset
                vm.onEvent(GalleryEvent.OnAppBarCollapseChanged(isCollapsed))
            }
        }
        binding.appBarLayout.setExpanded(!vm.state.value.isAppBarCollapsed)
    }

    private fun setShouldShowScrollBar(shouldShow: Boolean) {
        binding.recyclerView.isVerticalScrollBarEnabled = shouldShow
    }

    private fun setupRecyclerView() {
        if (!this::adapter.isInitialized) {
            adapter = GalleryAdapter(
                onLongClick = {
                    showImageBottomSheetDialog(imagePosition = it)
                },
                onClick = {
                    onImageClicked(imagePosition = it)
                }
            )
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(GalleryItemDecoration(8))
    }

    private fun onImageClicked(imagePosition: Int) {
        val navArgs = FragmentGalleryDirections.actionFragmentGalleryToFragmentImage(
            imagePosition = imagePosition
        )

        findNavController().navigate(navArgs)
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
                withContext(Dispatchers.Main) {
                    setShouldShowScrollBar(state.isAppBarCollapsed)
                }
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
            setToolbarDynamicTitle(images)
            adapter.submitList(images)
        }
    }

    private fun setToolbarDynamicTitle(images: List<Image>) {
        if (images.isNotEmpty()) {
            val requestOptions = RequestOptions()
                .override(300, 300)
            Glide.with(requireContext())
                .load(images[0].contentUri)
                .apply(requestOptions)
                .into(binding.ivFirstImage)

            binding.toolbar.setTitle("Images")
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
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            var images = mutableListOf<Image>()

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
                val mimeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                val dateAddedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                val dateModColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)

                images = ArrayList<Image>(cursor.count)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val size = cursor.getInt(sizeColumn)
                    val mime = cursor.getString(mimeColumn)
                    val dateAdded = cursor.getLong(dateAddedColumn)
                    val dateMod = cursor.getLong(dateModColumn)

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
                            contentUri = contentUri,
                            mime = mime,
                            dateAdded = dateAdded,
                            dateModified = dateMod
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

    private fun showImageBottomSheetDialog(imagePosition: Int) {
        val image = vm.state.value.images[imagePosition]
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = BottomSheetImageInfoBinding.inflate(layoutInflater)

        val locale = resources.configuration.locales[0]

        val title = String.format(locale, "Image name: %s", image.displayName)
        val mediaType = String.format(locale, "Media Type: %s", image.mime)
        val mediaResolution = String.format(locale, "Resolution: %dx%d", image.width, image.height)

        bottomSheetView.tvMediaTitle.text = title
        bottomSheetView.tvMediaType.text = mediaType
        bottomSheetView.tvMediaResolution.text = mediaResolution

        bottomSheetDialog.setContentView(bottomSheetView.root)
        bottomSheetDialog.show()
    }
}