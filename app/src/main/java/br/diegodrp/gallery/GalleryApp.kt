package br.diegodrp.gallery

import android.app.Application
import br.diegodrp.gallery.di.mainModule
import com.google.android.material.color.DynamicColors
import org.koin.core.context.startKoin

class GalleryApp: Application() {

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        startKoin {
            modules(mainModule)
        }
    }
}