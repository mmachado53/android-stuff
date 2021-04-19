package com.mmachado53.simplemvvmapp.data.model.apiresponses

import android.content.ClipData
import java.util.Date

data class ItemsApiResponse(
    val items: List<ClipData.Item>,
    val deletedItemsIds: List<Long>,
    val lastSyncDate: Date
)
