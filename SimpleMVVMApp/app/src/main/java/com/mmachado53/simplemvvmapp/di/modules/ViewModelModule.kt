package com.mmachado53.simplemvvmapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mmachado53.simplemvvmapp.ui.items.ItemsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ItemsListViewModel::class)
    internal abstract fun itemsListViewModel(viewModel: ItemsListViewModel): ViewModel
}