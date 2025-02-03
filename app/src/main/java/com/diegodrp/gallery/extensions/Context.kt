package com.diegodrp.gallery.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.ContextCompat
import com.diegodrp.gallery.helpers.PERMISSION_READ_IMAGES
import com.diegodrp.gallery.helpers.PERMISSION_READ_STORAGE
import com.diegodrp.gallery.helpers.PERMISSION_READ_VIDEOS
import com.diegodrp.gallery.helpers.PERMISSION_WRITE_STORAGE
import com.diegodrp.gallery.helpers.Permission

fun Context.hasPermission(permission: Permission): Boolean {
   return ContextCompat.checkSelfPermission(this, getPermissionString(permission.getId())) == PackageManager.PERMISSION_GRANTED
}

fun Context.getPermissionString(id: Int) = when (id) {
    PERMISSION_READ_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
    PERMISSION_WRITE_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE
    PERMISSION_READ_IMAGES -> if (isTiramisuPlus()) Manifest.permission.READ_MEDIA_IMAGES else ""
    PERMISSION_READ_VIDEOS -> if (isTiramisuPlus()) Manifest.permission.READ_MEDIA_VIDEO else ""
    else -> ""
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
fun isOreoV2Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
fun isPiePlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isQPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
fun isRPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isSPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
fun isSV2Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
fun isTiramisuPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun isUpsideDownCakePlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun isVanillaIceCreamPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM

@ChecksSdkIntAtLeast(api = 36)
fun isBaklavaPlus() = Build.VERSION.SDK_INT >= 36


