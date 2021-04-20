package com.mmachado53.simplemvvmapp.di.modules

import com.mmachado53.simplemvvmapp.data.ItemRepository
import com.mmachado53.simplemvvmapp.data.ItemRepositoryInterface
import com.mmachado53.simplemvvmapp.data.local.ItemLocalDataSourceInterface
import com.mmachado53.simplemvvmapp.data.remote.ItemRemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideItemRepository(localDataSource: ItemLocalDataSourceInterface): ItemRepositoryInterface {
        return ItemRepository(localDataSource, ItemRemoteDataSource())
    }
}
