package br.diegodrp.gallery.view.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.diegodrp.gallery.databinding.ActivityMainBinding
import br.diegodrp.gallery.permission.OnPermissionRequestedCallback
import br.diegodrp.gallery.permission.PermissionFinder
import br.diegodrp.gallery.view.permission.isPermissionGranted
import br.diegodrp.gallery.view.permission.permissionDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.iterator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val onPermissionRequestedCallbacks = mutableListOf<OnPermissionRequestedCallback>()

    private val requestPermissionActivity = registerForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        callback = { permissions ->
            for (permission in permissions) {
                if (!permission.value && shouldShowRequestPermissionRationale(permission.key)) {
                    permissionDialog(
                        permission = PermissionFinder.findPermission(permission.key),
                        onDismiss = { it?.dismiss() },
                        onOk = { it?.dismiss() },
                        isDeclined = true
                    )
                }
                callPermissionRequestedCallback(permission.key, permission.value)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
    }

    /* Permission Stuff Below */

    fun requestStoragePermissions() {
        val permissions = getStoragePermissions()
        requestPermissionsShowingDialog(permissions)
    }

    fun isStoragePermissionAllowed(): Boolean {
        val permissions = getStoragePermissions()

        for (permission in permissions) {
            if (!isPermissionGranted(permission)) return false
        }

        return true
    }

    private fun requestPermissionsShowingDialog(permissions: MutableList<String>) {
        lifecycleScope.launch(Dispatchers.IO) {
            val iterator = permissions.iterator()
            val dialogCount = permissions.size
            var dialogDismissedCount = 0

            while (iterator.hasNext()) {
                val permission = iterator.next()
                if (isPermissionGranted(permission)) {
                    iterator.remove()
                } else if (!isPermissionGranted(permission) && shouldShowRequestPermissionRationale(
                        permission
                    )
                ) {
                    withContext(Dispatchers.Main) {
                        permissionDialog(
                            permission = PermissionFinder.findPermission(permission),
                            onDismiss = { it?.dismiss(); dialogDismissedCount++ },
                            onOk = { it?.dismiss() }
                        )
                    }
                } else if (!shouldShowRequestPermissionRationale(permission)) {
                    dialogDismissedCount++
                }
            }

            // wait until all dialogs are closed.
            while (dialogCount != dialogDismissedCount) {
                delay(100L)
            }

            withContext(Dispatchers.Main) {
                requestPermissionActivity.launch(permissions.toTypedArray())
            }
        }
    }

    fun registerOnPermissionRequested(
        onPermissionRequested: OnPermissionRequestedCallback
    ) {
        onPermissionRequestedCallbacks.add(onPermissionRequested)
    }

    fun unregisterOnPermissionRequested(
        onPermissionRequested: OnPermissionRequestedCallback
    ) {
        onPermissionRequestedCallbacks.remove(onPermissionRequested)
    }

    private fun callPermissionRequestedCallback(permission: String, isGranted: Boolean) {
        for (callback in onPermissionRequestedCallbacks) {
            callback?.onPermissionRequested(permission, isGranted)
        }
    }

    private fun getStoragePermissions(): MutableList<String> {
        return if (Build.VERSION.SDK_INT <= 28) {
            mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else if (Build.VERSION.SDK_INT <= 32) {
            mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else if (Build.VERSION.SDK_INT >= 33) {
            mutableListOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            throw IllegalStateException("Unknown SDK version of ${Build.VERSION.SDK_INT}")
        }
    }
}