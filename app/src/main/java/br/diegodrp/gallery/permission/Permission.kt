package br.diegodrp.gallery.permission

import android.Manifest

class PermissionFinder {
    companion object {
        fun findPermission(permission: String): Permission {
            return when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    ReadExternalStoragePermission()
                }
                Manifest.permission.READ_MEDIA_IMAGES -> {
                    ReadMediaImagesPermission()
                }
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    WriteExternalStoragePermission()
                }
                else -> throw IllegalStateException("Unknown permission")
            }
        }
    }
}

interface Permission {
    fun getPermissionRationaleMessage(): String
    fun getPostDeclineMessage(): String
}

class ReadExternalStoragePermission: Permission {
    override fun getPermissionRationaleMessage(): String {
        return "This app requires reading the storage so that it's able to show " +
                "your images in the Gallery. Refusing it will make the app unusable."
    }

    override fun getPostDeclineMessage(): String {
        return "Without the permission to read storage, the app won't be able " +
                "to work."
    }
}

class WriteExternalStoragePermission: Permission {
    override fun getPermissionRationaleMessage(): String {
        return "This app requires writing the storage so that it's able to show " +
                "your images in the Gallery. Refusing it will make the app unusable."
    }

    override fun getPostDeclineMessage(): String {
        return "Without the permission to write storage, the app won't be able " +
                "to work."
    }
}

class ReadMediaImagesPermission: Permission {
    override fun getPermissionRationaleMessage(): String {
        return "This app requires reading image medias so that it's able to show " +
                "your images in the Gallery. Refusing it will make the app unusable."
    }

    override fun getPostDeclineMessage(): String {
        return "Without the permission to read images, the app won't be able " +
                "to work."
    }
}