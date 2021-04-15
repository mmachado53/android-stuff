package com.mmachado53.simplemvvmapp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mmachado53.simplemvvmapp.commons.test.getOrAwaitValue
import com.mmachado53.simplemvvmapp.data.local.ItemLocalDataSource
import com.mmachado53.simplemvvmapp.data.local.ItemLocalDataSourceInterface
import com.mmachado53.simplemvvmapp.data.local.dao.AppDataBase
import com.mmachado53.simplemvvmapp.data.model.Item
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class ItemLocalDataSourceTest {

    private lateinit var itemLocalDataSource: ItemLocalDataSourceInterface

    private lateinit var dataBase: AppDataBase

    private lateinit var preferences: SharedPreferences

    // Be sure to run everything synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        preferences = context.getSharedPreferences("TEST_PREFERENCES", MODE_PRIVATE)
        dataBase = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .setQueryExecutor(TestCoroutineDispatcher().asExecutor()).build()
        itemLocalDataSource = ItemLocalDataSource(dataBase.itemDao(), preferences)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        dataBase.close()
    }

    /**
     * Verifies that it is possible to insert [Item]´s correctly
     * */
    @Test
    fun insertAndSelect() = runBlocking {
        dataBase.clearAllTables() // make sure there is no data
        val item1 = Item(serverId = null, localId = null, content = "Hello World")
        val item2 = Item(serverId = null, localId = null, content = "Hello World 2")
        itemLocalDataSource.insertItems(item1, item2)

        val allItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()!!
        val deletedItems = itemLocalDataSource.deletedItems()
        val savedItem1 = itemLocalDataSource.itemByLocalId(allItems[0].localId!!)
        val savedItem2 = itemLocalDataSource.itemByLocalId(allItems[1].localId!!)

        MatcherAssert.assertThat("There should be no deleted items", deletedItems.isEmpty())
        MatcherAssert.assertThat("Total of items is wrong", allItems.size == 2)
        MatcherAssert.assertThat("Item1 was not saved correctly", savedItem1?.content == "Hello World")
        MatcherAssert.assertThat("Item2 was not saved correctly", savedItem2?.content == "Hello World 2")
    }

    /**
     * Verifies that it is possible to update [Item]´s correctly
     * */
    @Test
    fun insertAndUpdate() = runBlocking {
        dataBase.clearAllTables() // make sure there is no data
        val item1 = Item(serverId = null, localId = null, content = "Hello World")
        val item2 = Item(serverId = null, localId = null, content = "Hello World 2")
        itemLocalDataSource.insertItems(item1, item2)
        val allItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()!!
        val modifiedItem1 = Item(allItems[0].localId, 1L, "Hello World UPDATED")
        val modifiedItem2 = Item(allItems[1].localId, 2L, "Hello World 2 UPDATED")

        itemLocalDataSource.updateItems(modifiedItem1, modifiedItem2)
        val allUpdatedItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()!!

        MatcherAssert.assertThat("Total of items is wrong", allUpdatedItems.size == 2)
        val foundItem1 = allUpdatedItems.find { it.content == "Hello World UPDATED" && it.serverId == 1L } != null
        MatcherAssert.assertThat("Item1 was not updated correctly", foundItem1)
        val foundItem2 = allUpdatedItems.find { it.content == "Hello World 2 UPDATED" && it.serverId == 2L } != null
        MatcherAssert.assertThat("Item2 was not updated correctly", foundItem2)
    }

    /**
     * Verifies that it is possible to remove [Item]´s in a soft way
     * */
    @Test
    fun softDelete() = runBlocking {
        dataBase.clearAllTables() // make sure there is no data
        val item1 = Item(serverId = null, localId = null, content = "Hello World")
        val item2 = Item(serverId = null, localId = null, content = "Hello World 2")
        itemLocalDataSource.insertItems(item1, item2)
        val allItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()!!

        itemLocalDataSource.softDeleteItems(allItems[0], allItems[1])

        val deletedItems = itemLocalDataSource.deletedItems()
        val notDeletedItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()
        MatcherAssert.assertThat("Total of deleted items is wrong", deletedItems.size == 2)
        MatcherAssert.assertThat("There are still items not deleted", notDeletedItems.isEmpty())
    }

    /**
     * Verifies that the conflict resolution strategy is to replace when trying to insert an [Item] with an existing [Item.serverId]
     * */
    @Test
    fun reinsertReplaceStrategy() = runBlocking {
        dataBase.clearAllTables() // make sure there is no data
        val item1 = Item(serverId = 1L, localId = null, content = "Hello World")
        val item2 = Item(serverId = 2L, localId = null, content = "Hello World 2")
        itemLocalDataSource.insertItems(item1, item2)
        var itemToReinsert = Item(serverId = 1, localId = null, content = "Hello World Reinserted")

        itemLocalDataSource.insertItems(itemToReinsert)
        val allItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()
        val reinsertedItem = allItems.find { it.serverId == 1L }

        MatcherAssert.assertThat("Total of items is wrong", allItems.size == 2)
        MatcherAssert.assertThat("There are still items not deleted", reinsertedItem?.content == "Hello World Reinserted")
    }

    /**
     * Verifies that it is possible to remove [Item]´s in a hard way
     * */
    @Test
    fun hardDelete() = runBlocking {
        dataBase.clearAllTables() // make sure there is no data
        val item1 = Item(serverId = null, localId = null, content = "Hello World")
        val item2 = Item(serverId = null, localId = null, content = "Hello World 2")
        itemLocalDataSource.insertItems(item1, item2)
        val allItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()!!

        itemLocalDataSource.deleteItems(allItems[0], allItems[1])

        val deletedItems = itemLocalDataSource.deletedItems()
        val notDeletedItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()
        MatcherAssert.assertThat("There shouldn't be Items", deletedItems.isEmpty())
        MatcherAssert.assertThat("There shouldn't be deleted Items", notDeletedItems.isEmpty())
    }

    /**
     * Verifies that it is possible to remove [Item]´s in a hard way by server_id
     * */
    @Test
    fun deleteByServerIdTest() = runBlocking {
        dataBase.clearAllTables() // make sure there is no data
        val item1 = Item(serverId = 43, localId = null, content = "Hello World")
        val item2 = Item(serverId = 50, localId = null, content = "Hello World 2")
        itemLocalDataSource.insertItems(item1, item2)

        itemLocalDataSource.deleteItemsByServerId(43L, 50, 51)

        val deletedItems = itemLocalDataSource.deletedItems()
        val notDeletedItems = itemLocalDataSource.allItemsLiveData().getOrAwaitValue()
        MatcherAssert.assertThat("There shouldn't be Items", deletedItems.isEmpty())
        MatcherAssert.assertThat("There shouldn't be deleted Items", notDeletedItems.isEmpty())
    }

    /**
     * Verifies that the filter to get the unsynchronized [Item]´s works correctly
     * */
    @Test
    fun notSynchronizedItems() = runBlocking {
        dataBase.clearAllTables() // make sure there is no data
        val item1 = Item(serverId = 1, localId = null, content = "Hello World", isSync = true)
        val item2 = Item(serverId = null, localId = null, content = "Hello World 2")
        itemLocalDataSource.insertItems(item1, item2)

        val notSynchronizedItems = itemLocalDataSource.notSynchronizedItems()

        MatcherAssert.assertThat("There must be an out-of-sync item", notSynchronizedItems.size == 1)
    }

    @Test
    fun getNullLastItemsSynchronizationDateTest() = runBlocking {
        preferences.edit().clear() // make sure there is no data

        val lastItemsSynchronizationDate = itemLocalDataSource.getLastItemsSynchronizationDate()

        MatcherAssert.assertThat("There should be no data", lastItemsSynchronizationDate == null)
    }

    @Test
    fun setAndGetLastItemsSynchronizationDateTest() = runBlocking {
        preferences.edit().clear() // make sure there is no data
        val expectedDate = Date(1618800669080)

        itemLocalDataSource.storeLastItemsSynchronizationDate(expectedDate)
        val lastItemsSynchronizationDate = itemLocalDataSource.getLastItemsSynchronizationDate()

        MatcherAssert.assertThat("The saved date does not correct", lastItemsSynchronizationDate?.time == 1618800669080)
    }
}
