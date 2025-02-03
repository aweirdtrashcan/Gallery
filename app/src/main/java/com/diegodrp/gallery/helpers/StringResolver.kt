package com.diegodrp.gallery.helpers

import android.content.Context
import androidx.annotation.StringRes

class StringResolver(private val context: Context) {
    fun getString(@StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }
}