package com.diegodrp.gallery.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.diegodrp.gallery.data.MediaRepository
import com.diegodrp.gallery.databinding.ActivityMainBinding
import com.diegodrp.gallery.extensions.hasPermission
import com.diegodrp.gallery.extensions.viewBinding
import com.diegodrp.gallery.helpers.PERMISSION_READ_IMAGES
import com.diegodrp.gallery.helpers.ReadImages
import com.diegodrp.gallery.helpers.Resource
import com.diegodrp.gallery.view.permission.PermissionActivity
import com.diegodrp.gallery.view.permission.showPermissionDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : PermissionActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    @Inject
    lateinit var repo: MediaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        if (!hasPermission(ReadImages)) {
            showPermissionDialog(ReadImages) {
                handlePermission(ReadImages) { isGranted: Boolean ->
                    if (isGranted) {
                        Toast.makeText(this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}