package br.diegodrp.gallery

import android.app.Application
import br.diegodrp.gallery.di.mainModule
import org.koin.core.context.startKoin

class GalleryApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(mainModule)
        }
    }
}