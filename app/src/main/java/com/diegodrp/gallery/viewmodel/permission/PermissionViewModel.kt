package com.diegodrp.gallery.viewmodel.permission

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegodrp.gallery.helpers.Permission
import com.diegodrp.gallery.view.permission.showPermissionDialog
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

    fun showDialogs(context: Context) {
        val permissionWithCallback = permissionDialogs.getOrNull(permissionDialogs.lastIndex) ?: return

        permissionDialogs.remove(permissionWithCallback)

        val onAllDialogsDismissed = permissionWithCallback.keys.first()
        val permissions = permissionWithCallback.values.first()

        val totalPermissions = permissions.size
        var permissionsDismissed = 0

        for (permission in permissions) {
            context.showPermissionDialog(permission) {
                permissionsDismissed++
                if (permissionsDismissed == totalPermissions) {
                    onAllDialogsDismissed()
                }
            }
        }
    }
}