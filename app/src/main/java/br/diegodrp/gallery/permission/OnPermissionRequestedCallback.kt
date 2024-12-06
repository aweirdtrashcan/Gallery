package br.diegodrp.gallery.permission

interface OnPermissionRequestedCallback {
    fun onPermissionRequested(permission: String, isGranted: Boolean)
}