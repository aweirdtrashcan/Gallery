package br.diegodrp.gallery.view.permission

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import br.diegodrp.gallery.permission.Permission
import br.diegodrp.gallery.databinding.AlertDialogPermissionBinding

fun Activity.permissionDialog(
    permission: Permission,
    onDismiss: (dialog: AlertDialog?) -> Unit,
    onOk: (dialog: AlertDialog?) -> Unit,
    isDeclined: Boolean = false
): AlertDialog {

    val dialogView = AlertDialogPermissionBinding.inflate(layoutInflater)

    var builtAlertDialog: AlertDialog? = null

    val alertDialog = AlertDialog.Builder(this)
        .setView(dialogView.root)
        .setOnDismissListener { onDismiss(builtAlertDialog) }
        .create()

    dialogView.btnAction.setOnClickListener {
        onOk(builtAlertDialog)
    }

    dialogView.btnAction.text = if (isDeclined) "I Understand" else "Ok"
    dialogView.tvTitle.text = "Permission required"
    dialogView.tvDescription.text = if (isDeclined) permission.getPostDeclineMessage()
    else permission.getPermissionRationaleMessage()

    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    return alertDialog.also {
        it.show()
        builtAlertDialog = it
    }
}

interface PermissionTextProvider {
    fun getDescription(): String
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(): String {
        return ""
    }
}