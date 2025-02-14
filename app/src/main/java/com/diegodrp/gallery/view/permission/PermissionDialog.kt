package com.diegodrp.gallery.view.permission

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.diegodrp.gallery.helpers.Permission

fun Context.showPermissionDialog(permission: Permission, onDismissed: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle("Permission Required")
        .setMessage(permission.getMessage())
        .setOnDismissListener { onDismissed() }
        .setPositiveButton("Ok") { _, _ -> }
        .show()
}
