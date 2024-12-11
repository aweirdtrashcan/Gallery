package br.diegodrp.gallery.di

import br.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import org.koin.dsl.module

val mainModule = module {
    single {
        GalleryViewModel()
    }
}