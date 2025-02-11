package com.diegodrp.gallery.view.image

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentImageBinding

class ImageFragment: Fragment(R.layout.fragment_image) {

    private lateinit var binding: FragmentImageBinding
    private val args by navArgs<ImageFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()

        Glide.with(requireContext())
            .load(args.image.uri)
            .into(binding.ivImage)
    }
}