package com.diegodrp.gallery.view.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.diegodrp.gallery.extensions.getPermissionString
import com.diegodrp.gallery.extensions.hasPermission
import com.diegodrp.gallery.helpers.Permission

abstract class PermissionActivity: AppCompatActivity() {

    private var actionOnPermission: ((isGranted: Boolean) -> Unit)? = null
    private var isAskingPermission = false

    fun handlePermission(permission: Permission, callback: (isGranted: Boolean) -> Unit) {
        actionOnPermission = null
        if (hasPermission(permission)) callback(true)
        else {
            isAskingPermission = true
            actionOnPermission = callback
            ActivityCompat.requestPermissions(this, arrayOf(getPermissionString(permission.getId())), 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        isAskingPermission = false
        if (requestCode == 1 && permissions.isNotEmpty()) {
            actionOnPermission?.invoke(grantResults[0] == 0)
        }
    }

}