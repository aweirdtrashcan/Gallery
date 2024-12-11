package br.diegodrp.gallery.view.image

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.diegodrp.gallery.R
import br.diegodrp.gallery.databinding.FragmentImageBinding
import br.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import org.koin.android.ext.android.inject
import br.diegodrp.gallery.model.Image
import com.bumptech.glide.Glide

class FragmentImage: Fragment(R.layout.fragment_image) {

    private lateinit var binding: FragmentImageBinding
    private val args by navArgs<FragmentImageArgs>()
    private val viewModel by inject<GalleryViewModel>()
    private val glide by lazy {
        Glide.with(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageBinding.bind(view)

        loadImage()
    }

    private fun loadImage() {
        if (args.imageId == -1) {
            findNavController().popBackStack()
            return
        }

        val image = viewModel.state.value.images.find {
            it.id.toInt() == args.imageId
        }

        // shouldn't happen any error by now, but just in case..
        if (image == null) {
            findNavController().popBackStack()
            return
        }

        glide.load(image.contentUri)
            .into(binding.imageView)
    }
}