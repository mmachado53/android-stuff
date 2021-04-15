package com.mmachado53.simplemvvmapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mmachado53.simplemvvmapp.data.model.Item

@Dao
abstract class ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAsync(vararg items: Item)

    @Update
    abstract suspend fun updateAsync(vararg items: Item)

    @Query("SELECT * FROM items WHERE local_id = :localId LIMIT 1")
    abstract suspend fun itemByLocalIdAsync(localId: Long): Item?

    @Delete
    abstract suspend fun deleteAsync(vararg items: Item)

    @Query("DELETE FROM items WHERE server_id IN (:serverIds)")
    abstract suspend fun deleteByServerIds(vararg serverIds: Long)

    suspend fun softDeleteAsync(vararg items: Item) {
        items.forEach { it.deleted = true }
        updateAsync(*items)
    }

    @Query("SELECT * FROM items WHERE deleted = 0 ORDER BY local_id ASC")
    abstract fun allNotDeletedItemsLiveData(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE deleted = 1 ORDER BY local_id ASC")
    abstract suspend fun allDeletedItemsAsync(): List<Item>

    @Query("SELECT * FROM items WHERE is_sync = 0 ORDER BY local_id ASC")
    abstract suspend fun allNotSynchronizedItemsAsync(): List<Item>
}
