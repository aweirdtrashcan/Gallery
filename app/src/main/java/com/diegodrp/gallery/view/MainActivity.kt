package com.diegodrp.gallery.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.diegodrp.gallery.databinding.ActivityMainBinding
import com.diegodrp.gallery.extensions.hasPermission
import com.diegodrp.gallery.extensions.viewBinding
import com.diegodrp.gallery.helpers.Permission
import com.diegodrp.gallery.helpers.ReadImages
import com.diegodrp.gallery.helpers.ReadVideos
import com.diegodrp.gallery.view.permission.PermissionActivity
import com.diegodrp.gallery.view.permission.showPermissionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : PermissionActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val backPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val hasPoppedBackStack =
                    binding.fragmentContainerView.findNavController().popBackStack()
                if (!hasPoppedBackStack) {
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        setupActionBar()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun setupActionBar() {
        val navController = binding.fragmentContainerView.findNavController()
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)

        onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}