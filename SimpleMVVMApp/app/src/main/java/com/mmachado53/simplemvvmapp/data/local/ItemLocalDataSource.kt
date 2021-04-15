package com.mmachado53.simplemvvmapp.data.local

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.mmachado53.simplemvvmapp.data.local.dao.ItemDao
import com.mmachado53.simplemvvmapp.data.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class ItemLocalDataSource(
    private val itemDao: ItemDao,
    private val preferences: SharedPreferences
) : ItemLocalDataSourceInterface {

    private companion object {
        private const val PREF_LAST_ITEMS_SYNC_DATE = "PREF_LAST_ITEMS_SYNC_DATE"
    }

    override suspend fun insertItems(vararg items: Item) = withContext(Dispatchers.IO) {
        itemDao.insertAsync(*items)
    }

    override suspend fun updateItems(vararg items: Item) = withContext(Dispatchers.IO) {
        itemDao.updateAsync(*items)
    }

    override suspend fun itemByLocalId(localId: Long): Item? = withContext(Dispatchers.IO) {
        itemDao.itemByLocalIdAsync(localId)
    }

    override suspend fun deleteItems(vararg items: Item) = withContext(Dispatchers.IO) {
        itemDao.deleteAsync(*items)
    }

    override suspend fun deleteItemsByServerId(vararg serverIds: Long) = withContext(Dispatchers.IO) {
        itemDao.deleteByServerIds(*serverIds)
    }

    override suspend fun softDeleteItems(vararg items: Item) = withContext(Dispatchers.IO) {
        itemDao.softDeleteAsync(*items)
    }

    override fun allItemsLiveData(): LiveData<List<Item>> = itemDao.allNotDeletedItemsLiveData()

    override suspend fun deletedItems(): List<Item> = withContext(Dispatchers.IO) {
        return@withContext itemDao.allDeletedItemsAsync()
    }

    override suspend fun notSynchronizedItems(): List<Item> = withContext(Dispatchers.IO) {
        return@withContext itemDao.allNotSynchronizedItemsAsync()
    }

    override suspend fun storeLastItemsSynchronizationDate(date: Date) = withContext(Dispatchers.IO) {
        preferences.edit()
            .putLong(PREF_LAST_ITEMS_SYNC_DATE, date.time)
            .apply()
    }

    override suspend fun getLastItemsSynchronizationDate(): Date? {
        return if (preferences.contains(PREF_LAST_ITEMS_SYNC_DATE))
            Date(preferences.getLong(PREF_LAST_ITEMS_SYNC_DATE, 0))
        else
            null
    }
}
