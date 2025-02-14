package com.diegodrp.gallery.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.diegodrp.gallery.helpers.Permission
import com.diegodrp.gallery.helpers.getPermissionString

fun Context.hasPermission(permission: Permission): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        getPermissionString(permission.getId())
    ) == PackageManager.PERMISSION_GRANTED
}


