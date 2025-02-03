package com.diegodrp.gallery.model

data class Album(
    val name: String,
    val images: List<Image>,
    val videos: List<Video>
)