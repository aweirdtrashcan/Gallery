package com.diegodrp.gallery.helpers

sealed interface Resource<T> {
    data class Success<T>(val data: T, val error: String? = null): Resource<T>
    data class Error<T>(val data: T? = null, val error: String): Resource<T>
    data class Loading<T>(val isLoading: Boolean, val data: T? = null, val message: String? = null): Resource<T>
}