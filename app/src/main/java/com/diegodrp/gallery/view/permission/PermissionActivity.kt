package com.diegodrp.gallery.view.permission

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.diegodrp.gallery.extensions.hasPermission
import com.diegodrp.gallery.helpers.Permission
import com.diegodrp.gallery.helpers.UnknownPermission
import com.diegodrp.gallery.helpers.getPermissionFromString
import com.diegodrp.gallery.helpers.getPermissionString
import com.diegodrp.gallery.viewmodel.permission.PermissionViewModel
import dagger.hilt.android.AndroidEntryPoint

typealias PermissionResult = (permission: Permission, isGranted: Boolean) -> Unit

@AndroidEntryPoint
abstract class PermissionActivity : AppCompatActivity() {

    private var permissionResultCallback: PermissionResult? = null
    private var isAskingPermission = false
    private val vm by viewModels<PermissionViewModel>()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        for (permission in permissions) {
            if (permission.value) {
                permissionResultCallback?.invoke(
                    getPermissionFromString(permission.key) ?: UnknownPermission,
                    permission.value
                )
            }
        }
    }

    fun handlePermissions(permissions: List<Permission>, callback: PermissionResult) {
        val permissionsToRequest = mutableListOf<Permission>()

        permissions.forEach { perm ->
            if (hasPermission(perm)) {
                callback(perm, true)
            } else {
                permissionsToRequest.add(perm)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            isAskingPermission = true
            vm.addPermissions(permissions) {
                permissionResultCallback = callback
                permissionLauncher.launch(
                    permissionsToRequest.map { getPermissionString(it.getId()) }.toTypedArray()
                )
            }
            vm.showDialogs { permission, onDismiss ->
                showPermissionDialog(permission, onDismiss)
            }
        }
    }
}