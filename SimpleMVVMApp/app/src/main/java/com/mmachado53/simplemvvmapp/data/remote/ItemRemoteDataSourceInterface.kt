package com.mmachado53.simplemvvmapp.data.remote

import com.mmachado53.simplemvvmapp.commons.exceptions.RemoteAPIException
import com.mmachado53.simplemvvmapp.data.model.Item
import com.mmachado53.simplemvvmapp.data.model.apiresponses.CreateItemApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.DeleteItemApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.ItemsApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.UpdateItemApiResponse
import java.util.Date
import kotlin.jvm.Throws

interface ItemRemoteDataSourceInterface {
    @Throws(RemoteAPIException::class)
    suspend fun getItems(lastSyncDate: Date?): ItemsApiResponse

    @Throws(RemoteAPIException::class)
    suspend fun createItem(item: Item): CreateItemApiResponse

    @Throws(RemoteAPIException::class)
    suspend fun updateItem(item: Item): UpdateItemApiResponse

    @Throws(RemoteAPIException::class)
    suspend fun deleteItem(item: Item): DeleteItemApiResponse
}
