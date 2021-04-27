package com.mmachado53.simplemvvmapp.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    indices = [
        Index(value = ["server_id"], unique = true)
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    var localId: Long?,
    @ColumnInfo(name = "server_id") var serverId: Long?,
    var content: String,
    var image: Bitmap? = null,
    @ColumnInfo(name = "is_sync")
    var isSync: Boolean = false,
    var deleted: Boolean = false
)
