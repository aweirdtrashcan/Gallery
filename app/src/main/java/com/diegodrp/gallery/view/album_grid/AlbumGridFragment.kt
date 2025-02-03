package com.diegodrp.gallery.view.album_grid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.diegodrp.gallery.R
import com.diegodrp.gallery.databinding.FragmentAlbumGridBinding
import com.diegodrp.gallery.model.Album

class AlbumGridFragment: Fragment(R.layout.fragment_album_grid) {

    private lateinit var binding: FragmentAlbumGridBinding
    private lateinit var adapter: AlbumGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlbumGridBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()

        adapter = AlbumGridAdapter(this::onAlbumClicked)
        binding.rvAlbums.adapter = adapter
        binding.rvAlbums.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)


    }

    private fun onAlbumClicked(album: Album) {

    }

}