package com.mmachado53.simplemvvmapp.di.modules

import com.mmachado53.simplemvvmapp.ui.items.ItemsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun itemsListFragment(): ItemsListFragment
}
