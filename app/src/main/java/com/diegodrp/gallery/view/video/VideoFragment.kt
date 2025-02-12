package com.diegodrp.gallery.view.video

import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.navArgs
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentVideoBinding

class VideoFragment: Fragment(R.layout.fragment_video) {

    private lateinit var binding: FragmentVideoBinding
    private val args by navArgs<VideoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentVideoBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()

        val videoPlayer = ExoPlayer.Builder(requireContext()).build()

        binding.playerView.apply {
            player = videoPlayer
            val mediaItem = MediaItem.fromUri(args.video.uri)
            videoPlayer.setMediaItem(mediaItem)
            videoPlayer.prepare()
            videoPlayer.play()
        }
    }
}