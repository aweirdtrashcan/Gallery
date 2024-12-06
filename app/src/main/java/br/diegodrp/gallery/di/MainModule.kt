package br.diegodrp.gallery.di

import br.diegodrp.gallery.viewmodel.gallery.GalleryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel {
        GalleryViewModel()
    }
}