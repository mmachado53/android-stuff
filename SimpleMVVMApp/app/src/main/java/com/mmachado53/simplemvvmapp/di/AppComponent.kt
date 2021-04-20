package com.mmachado53.simplemvvmapp.di

import com.mmachado53.simplemvvmapp.App
import com.mmachado53.simplemvvmapp.di.modules.ApplicationDataModule
import com.mmachado53.simplemvvmapp.di.modules.FragmentModule
import com.mmachado53.simplemvvmapp.di.modules.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationDataModule::class,
        ViewModelModule::class,
        FragmentModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>
}
