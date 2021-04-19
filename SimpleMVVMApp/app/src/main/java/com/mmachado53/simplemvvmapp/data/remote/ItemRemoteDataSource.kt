package com.mmachado53.simplemvvmapp.data.remote

import com.mmachado53.simplemvvmapp.commons.exceptions.RemoteAPIException
import com.mmachado53.simplemvvmapp.data.model.Item
import com.mmachado53.simplemvvmapp.data.model.apiresponses.CreateItemApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.DeleteItemApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.ItemsApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.UpdateItemApiResponse
import kotlinx.coroutines.delay
import java.util.Date

class ItemRemoteDataSource : ItemRemoteDataSourceInterface {
    override suspend fun getItems(lastSyncDate: Date?): ItemsApiResponse {
        // TODO to be implemented
        delay(timeMillis = 2000)
        throw RemoteAPIException.ConnectionException
    }

    override suspend fun createItem(item: Item): CreateItemApiResponse {
        // TODO to be implemented
        delay(timeMillis = 2000)
        throw RemoteAPIException.ConnectionException
    }

    override suspend fun updateItem(item: Item): UpdateItemApiResponse {
        // TODO to be implemented
        delay(timeMillis = 2000)
        throw RemoteAPIException.ConnectionException
    }

    override suspend fun deleteItem(item: Item): DeleteItemApiResponse {
        // TODO to be implemented
        delay(timeMillis = 2000)
        throw RemoteAPIException.ConnectionException
    }
}
