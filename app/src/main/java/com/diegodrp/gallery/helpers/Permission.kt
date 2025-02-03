package com.diegodrp.gallery.helpers

const val PERMISSION_READ_STORAGE = 1
const val PERMISSION_WRITE_STORAGE = 2
const val PERMISSION_READ_IMAGES = 3
const val PERMISSION_READ_VIDEOS = 4

interface Permission {
    fun getId(): Int
    fun getMessage(): String
}

object ReadStorage: Permission {
    override fun getId() = PERMISSION_READ_STORAGE

    override fun getMessage(): String {
        return "Permission for Reading Storage is required for your images and videos to be shown in the grid. " +
                "Rejecting this permission will make the app unusable."
    }
}

object WriteStorage: Permission {
    override fun getId() = PERMISSION_WRITE_STORAGE

    override fun getMessage(): String {
        return "Permission for Writing Storage is required for your saving your images and videos to your phone. " +
                "Rejecting this permission will still make the app usable."
    }
}

object ReadImages: Permission {
    override fun getId() = PERMISSION_READ_IMAGES

    override fun getMessage(): String {
        return "Permission for Reading Images are required for your images to be shown in the grid. " +
                "Rejecting this permission will make the app unusable."
    }
}

object ReadVideos: Permission {
    override fun getId() = PERMISSION_READ_VIDEOS

    override fun getMessage(): String {
        return "Permission for Reading Videos are required for your videos to be shown in the grid. " +
                "Rejecting this permission will make the app unusable."
    }
}