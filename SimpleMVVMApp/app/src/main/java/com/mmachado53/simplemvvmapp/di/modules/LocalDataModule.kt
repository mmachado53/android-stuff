package com.mmachado53.simplemvvmapp.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.mmachado53.simplemvvmapp.data.local.ItemLocalDataSource
import com.mmachado53.simplemvvmapp.data.local.ItemLocalDataSourceInterface
import com.mmachado53.simplemvvmapp.data.local.dao.AppDataBase
import com.mmachado53.simplemvvmapp.data.local.dao.ItemDao
import com.mmachado53.simplemvvmapp.data.local.sharedpreferences.SharedPreferenceBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDataModule {
    @Provides
    @Singleton
    fun provideAppDataBase(context: Context): AppDataBase = AppDataBase.getInstance(context)

    @Provides
    fun provideItemDao(appDataBase: AppDataBase): ItemDao = appDataBase.itemDao()

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences = SharedPreferenceBuilder.build(context)

    @Provides
    fun provideItemsLocalDataSource(itemDao: ItemDao, preferences: SharedPreferences): ItemLocalDataSourceInterface {
        return ItemLocalDataSource(itemDao, preferences)
    }
}
