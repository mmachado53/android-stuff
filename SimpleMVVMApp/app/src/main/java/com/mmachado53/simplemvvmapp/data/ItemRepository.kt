package com.mmachado53.simplemvvmapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mmachado53.simplemvvmapp.commons.exceptions.RemoteAPIException
import com.mmachado53.simplemvvmapp.data.local.ItemLocalDataSourceInterface
import com.mmachado53.simplemvvmapp.data.model.Item
import com.mmachado53.simplemvvmapp.data.remote.ItemRemoteDataSourceInterface
import kotlin.jvm.Throws

class ItemRepository(
    private val localDataSource: ItemLocalDataSourceInterface,
    private val remoteDataSource: ItemRemoteDataSourceInterface
) : ItemRepositoryInterface {

    private val currentAPIException = MutableLiveData<RemoteAPIException?>()

    override suspend fun insertUpdateItem(item: Item) {
        try {
            if (item.serverId == null)
                item.serverId = remoteDataSource.createItem(item).createdItemId
            else
                remoteDataSource.updateItem(item)
            item.isSync = true
            currentAPIException.value = null
        } catch (remoteAPIException: RemoteAPIException) {
            item.isSync = false
            currentAPIException.value = remoteAPIException
            remoteAPIException.printStackTrace()
        } finally {
            if (item.localId == null)
                localDataSource.insertItems(item)
            else
                localDataSource.updateItems(item)
        }
    }

    override suspend fun syncItems() {
        try {
            sendNotSynchronizedDataToServer()
            retrieveNewDataFromServer()
            currentAPIException.value = null
        } catch (remoteAPIException: RemoteAPIException) {
            currentAPIException.value = remoteAPIException
            remoteAPIException.printStackTrace()
        }
    }

    @Throws(RemoteAPIException::class)
    private suspend fun sendNotSynchronizedDataToServer() {
        localDataSource.deletedItems().forEach { item ->
            remoteDataSource.deleteItem(item)
            localDataSource.deleteItems(item)
        }
        localDataSource.notSynchronizedItems().forEach { item ->
            if (item.serverId == null) {
                item.serverId = remoteDataSource.createItem(item).createdItemId
            } else {
                remoteDataSource.updateItem(item)
            }
            item.isSync = true
            localDataSource.updateItems(item)
        }
    }

    @Throws(RemoteAPIException::class)
    private suspend fun retrieveNewDataFromServer() {
        val lastItemsSynchronizationDate = localDataSource.getLastItemsSynchronizationDate()
        val getItemsApiResponse = remoteDataSource.getItems(lastItemsSynchronizationDate)
        val responseDeletedItemsIds = getItemsApiResponse.deletedItemsIds.toLongArray()
        localDataSource.deleteItemsByServerId(*responseDeletedItemsIds)
        val responseNewItems = getItemsApiResponse.items.map {
            it.deleted = false
            it.isSync = true
            it
        }.toTypedArray()
        localDataSource.insertItems(*responseNewItems)
        localDataSource.storeLastItemsSynchronizationDate(getItemsApiResponse.lastSyncDate)
    }

    override suspend fun removeItem(item: Item) {
        if (item.serverId == null) {
            localDataSource.deleteItems(item)
            return
        }
        try {
            remoteDataSource.deleteItem(item)
            localDataSource.deleteItems(item)
        } catch (remoteAPIException: RemoteAPIException) {
            localDataSource.softDeleteItems(item)
            currentAPIException.value = remoteAPIException
            remoteAPIException.printStackTrace()
        }
    }

    override suspend fun getItem(localId: Long): Item? = localDataSource.itemByLocalId(localId)

    override fun allItems(): LiveData<List<Item>> = localDataSource.allItemsLiveData()

    override fun currentRemoteApiException(): LiveData<RemoteAPIException?> = currentAPIException
}
