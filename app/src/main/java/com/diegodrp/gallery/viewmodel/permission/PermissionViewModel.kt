package com.diegodrp.gallery.viewmodel.permission

import androidx.lifecycle.ViewModel
import com.diegodrp.gallery.helpers.Permission
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {

    private val permissionDialogs = mutableListOf<Map<() -> Unit, List<Permission>>>()

    fun addPermissions(permissions: List<Permission>, onAllDialogsDismissed: () -> Unit) {
        val permissionWithCallback = mutableMapOf<() -> Unit, List<Permission>>()
        permissionWithCallback.getOrPut(onAllDialogsDismissed) { permissions }
        permissionDialogs.add(permissionWithCallback)
    }

    fun showDialogs(showDialogCallback: (permission: Permission, onDismiss: () -> Unit) -> Unit) {
        val permissionWithCallback = permissionDialogs.getOrNull(permissionDialogs.lastIndex) ?: return

        permissionDialogs.remove(permissionWithCallback)

        val onAllDialogsDismissed = permissionWithCallback.keys.first()
        val permissions = permissionWithCallback.values.first()

        val totalPermissions = permissions.size
        var permissionsDismissed = 0

        for (permission in permissions) {
            showDialogCallback(permission) {
                permissionsDismissed++
                if (permissionsDismissed == totalPermissions) {
                    onAllDialogsDismissed()
                }
            }
        }
    }
}