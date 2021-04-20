package com.mmachado53.simplemvvmapp.data.model.apiresponses

import com.mmachado53.simplemvvmapp.data.model.Item
import java.util.Date

data class ItemsApiResponse(
    val items: List<Item>,
    val deletedItemsIds: List<Long>,
    val lastSyncDate: Date
)
