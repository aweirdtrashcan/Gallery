package com.diegodrp.gallery.di

import android.content.Context
import com.diegodrp.gallery.data.MediaRepository
import com.diegodrp.gallery.data.MediaRepositoryImpl
import com.diegodrp.gallery.helpers.PreviewSizeCalculator
import com.diegodrp.gallery.helpers.StringResolver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideStringResolver(@ApplicationContext context: Context): StringResolver {
        return StringResolver(context)
    }

    @Singleton
    @Provides
    fun provideMediaRepository(@ApplicationContext context: Context, stringResolver: StringResolver): MediaRepository {
        return MediaRepositoryImpl(context, stringResolver)
    }

    @Singleton
    @Provides
    fun providePreviewSizeCalculator(): PreviewSizeCalculator {
        return PreviewSizeCalculator()
    }

}