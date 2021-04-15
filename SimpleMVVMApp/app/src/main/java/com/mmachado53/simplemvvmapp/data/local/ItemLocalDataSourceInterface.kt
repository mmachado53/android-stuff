package com.mmachado53.simplemvvmapp.data.local

import androidx.lifecycle.LiveData
import com.mmachado53.simplemvvmapp.data.model.Item
import java.util.Date

interface ItemLocalDataSourceInterface {
    suspend fun insertItems(vararg items: Item)
    suspend fun updateItems(vararg items: Item)
    suspend fun itemByLocalId(localId: Long): Item?
    suspend fun deleteItems(vararg items: Item)
    suspend fun deleteItemsByServerId(vararg serverIds: Long)
    suspend fun softDeleteItems(vararg items: Item)
    fun allItemsLiveData(): LiveData<List<Item>>
    suspend fun deletedItems(): List<Item>
    suspend fun notSynchronizedItems(): List<Item>
    suspend fun storeLastItemsSynchronizationDate(date: Date)
    suspend fun getLastItemsSynchronizationDate(): Date?
}
