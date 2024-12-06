package br.diegodrp.gallery.view.permission

import android.app.Activity
import android.content.pm.PackageManager

fun Activity.isPermissionGranted(permission: String): Boolean {
    return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}