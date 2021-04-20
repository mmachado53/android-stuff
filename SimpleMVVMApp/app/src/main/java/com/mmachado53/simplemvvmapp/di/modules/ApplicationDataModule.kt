package com.mmachado53.simplemvvmapp.di.modules

import android.content.Context
import com.mmachado53.simplemvvmapp.App
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        LocalDataModule::class,
        RepositoryModule::class
    ]
)
class ApplicationDataModule {
    @Provides
    fun provideContext(app: App): Context = app.applicationContext
}
