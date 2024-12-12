package br.diegodrp.gallery.model

class UnitConverter {
    fun fromBytesToMegabytes(byteCount: Int): Int {
        return byteCount / 1000 / 1000
    }

    fun fromBytesToMebibytes(byteCount: Int): Int {
        return byteCount / 1024 / 1024
    }
}