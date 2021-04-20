package com.mmachado53.simplemvvmapp.data

import androidx.lifecycle.LiveData
import com.mmachado53.simplemvvmapp.commons.exceptions.RemoteAPIException
import com.mmachado53.simplemvvmapp.data.model.Item

interface ItemRepositoryInterface {
    suspend fun insertUpdateItem(item: Item)
    suspend fun syncItems()
    suspend fun removeItem(item: Item)
    suspend fun getItem(localId: Long): Item?
    fun allItems(): LiveData<List<Item>>
    fun currentRemoteApiException(): LiveData<RemoteAPIException?>
}
