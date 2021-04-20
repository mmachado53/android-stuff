package com.mmachado53.simplemvvmapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mmachado53.simplemvvmapp.commons.exceptions.RemoteAPIException
import com.mmachado53.simplemvvmapp.data.ItemRepository
import com.mmachado53.simplemvvmapp.data.local.ItemLocalDataSourceInterface
import com.mmachado53.simplemvvmapp.data.model.Item
import com.mmachado53.simplemvvmapp.data.model.apiresponses.CreateItemApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.DeleteItemApiResponse
import com.mmachado53.simplemvvmapp.data.model.apiresponses.ItemsApiResponse
import com.mmachado53.simplemvvmapp.data.remote.ItemRemoteDataSourceInterface
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class ItemRepositoryTest {

    private lateinit var mockItemLocalDataSource: ItemLocalDataSourceInterface

    private lateinit var mockItemRemoteDataSource: ItemRemoteDataSourceInterface

    private lateinit var mockObserver: Observer<RemoteAPIException?>

    private lateinit var itemRepository: ItemRepository // subject of test

    // Be sure to run everything synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun prepare() {
        mockItemLocalDataSource = mock(ItemLocalDataSourceInterface::class.java)
        mockItemRemoteDataSource = mock(ItemRemoteDataSourceInterface::class.java)
        itemRepository = ItemRepository(mockItemLocalDataSource, mockItemRemoteDataSource)
        mockObserver = mock(Observer<RemoteAPIException?> { }::class.java)
        itemRepository.currentRemoteApiException().observeForever(mockObserver)
    }

    /**
     * [ItemRepository.insertUpdateItem] Test´s
     * */

    /**
     * Inserting a new [Item] when remote data source do not fail
     * */
    @Test
    fun testInsertItemRemoteDataSourceNotFail() = runBlocking {
        val item = Item(localId = null, serverId = null, "Content 1")
        val expectedItemToStore = Item(localId = null, serverId = 1, "Content 1", isSync = true)
        val expectedApiResponse = CreateItemApiResponse(createdItemId = 1)

        `when`(mockItemRemoteDataSource.createItem(item)).then { expectedApiResponse }

        itemRepository.insertUpdateItem(item)

        verify(mockItemRemoteDataSource, times(1)).createItem(item)
        verify(mockItemLocalDataSource, times(1)).insertItems(expectedItemToStore) // should insert the [Item] locally with a valid serverId and isSync equal to true
        verify(mockObserver, times(1)).onChanged(null) // should notify that there was no API exception
    }

    /**
     * Try inserting a new [Item] when remote data source fails with a [RemoteAPIException]
     * */
    @Test
    fun testInsertItemWithRemoteAPIException() = runBlocking {
        val item = Item(localId = null, serverId = null, "Content 1")
        val expectedException = RemoteAPIException("Some Message", throwable = null)
        val expectedItemToStore = Item(localId = null, serverId = null, "Content 1", isSync = false)

        `when`(mockItemRemoteDataSource.createItem(item)).thenThrow(expectedException)

        itemRepository.insertUpdateItem(item)

        verify(mockItemRemoteDataSource, times(1)).createItem(item)
        verify(mockItemLocalDataSource, times(1)).insertItems(expectedItemToStore) // should insert the [Item] locally with a null serverId and isSync equal to false
        verify(mockObserver, times(1)).onChanged(expectedException) // should notify the API exception
    }

    /**
     * Update a local but not synced [Item] when remote data source do not fail
     * */
    @Test
    fun testUpdateNotSyncedItemRemoteDataSourceNotFail() = runBlocking {
        val item = Item(localId = 1, serverId = null, "Content 1")
        val expectedItemToStore = Item(localId = 1, serverId = 1, "Content 1", isSync = true)
        val expectedApiResponse = CreateItemApiResponse(createdItemId = 1)

        `when`(mockItemRemoteDataSource.createItem(item)).then { expectedApiResponse }

        itemRepository.insertUpdateItem(item)

        verify(mockItemRemoteDataSource, times(1)).createItem(item)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedItemToStore) // should update the [Item] locally with a valid serverId and isSync equal to true
        verify(mockObserver, times(1)).onChanged(null) // should notify that there was no API exception
    }

    /**
     * Try update a local but not synced [Item] when remote data source fails with a [RemoteAPIException]
     * */
    @Test
    fun testUpdateNotSyncedItemWithRemoteAPIException() = runBlocking {
        val item = Item(localId = 1, serverId = null, "Content 1")
        val expectedException = RemoteAPIException("Some Message", throwable = null)
        val expectedItemToStore = Item(localId = 1, serverId = null, "Content 1", isSync = false)

        `when`(mockItemRemoteDataSource.createItem(item)).thenThrow(expectedException)

        itemRepository.insertUpdateItem(item)

        verify(mockItemRemoteDataSource, times(1)).createItem(item)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedItemToStore) // should update the [Item] locally with a null serverId and isSync equal to false
        verify(mockObserver, times(1)).onChanged(expectedException) // should notify the API exception
    }

    /**
     * Update a local and synced [Item] when remote data source do not fail
     * */
    @Test
    fun testUpdateSyncedItemRemoteDataSourceNotFail() = runBlocking {
        val item = Item(localId = 1, serverId = 2, "Content 1")
        val expectedItemToStore = Item(localId = 1, serverId = 2, "Content 1", isSync = true)
        val expectedApiResponse = CreateItemApiResponse(createdItemId = 1)

        `when`(mockItemRemoteDataSource.updateItem(item)).then { expectedApiResponse }

        itemRepository.insertUpdateItem(item)

        verify(mockItemRemoteDataSource, times(1)).updateItem(item)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedItemToStore) // should update the [Item] locally with a valid serverId and isSync equal to true
        verify(mockObserver, times(1)).onChanged(null) // should notify that there was no API exception
    }

    /**
     * Try update a local and synced [Item] when remote data source fails with a [RemoteAPIException]
     * */
    @Test
    fun testUpdateSyncedItemWithRemoteAPIException() = runBlocking {
        val item = Item(localId = 1, serverId = 2, "Content 1", isSync = true)
        val expectedException = RemoteAPIException("Some Message", throwable = null)
        val expectedItemToStore = Item(localId = 1, serverId = 2, "Content 1", isSync = false)

        `when`(mockItemRemoteDataSource.updateItem(item)).thenThrow(expectedException)

        itemRepository.insertUpdateItem(item)

        verify(mockItemRemoteDataSource, times(1)).updateItem(item)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedItemToStore) // should update the [Item] locally with a null serverId and isSync equal to false
        verify(mockObserver, times(1)).onChanged(expectedException) // should notify the API exception
    }

    /**
     * [ItemRepository.removeItem] Test´s
     * */

    @Test
    fun removeItemWithServerId() = runBlocking {
        val item = Item(localId = 1, serverId = 2, "Content 1", isSync = true)
        val expectedApiResponse = DeleteItemApiResponse(deletedItemId = 2)

        `when`(mockItemRemoteDataSource.deleteItem(item)).then { expectedApiResponse }

        itemRepository.removeItem(item)

        verify(mockItemRemoteDataSource, times(1)).deleteItem(item)
        verify(mockItemLocalDataSource, times(1)).deleteItems(item)
    }

    @Test
    fun removeItemWithServerIdWithRemoteAPIException() = runBlocking {
        val item = Item(localId = 1, serverId = 2, "Content 1", isSync = true)
        val expectedApiException = RemoteAPIException("Some Message", throwable = null)

        `when`(mockItemRemoteDataSource.deleteItem(item)).thenThrow(expectedApiException)

        itemRepository.removeItem(item)

        verify(mockItemRemoteDataSource, times(1)).deleteItem(item)
        verify(mockItemLocalDataSource, times(1)).softDeleteItems(item)
    }

    @Test
    fun removeItemWithoutServerId() = runBlocking {
        val item = Item(localId = 1, serverId = null, "Content 1", isSync = true)

        itemRepository.removeItem(item)

        verify(mockItemRemoteDataSource, times(0)).deleteItem(item)
        verify(mockItemLocalDataSource, times(0)).softDeleteItems(item)
        verify(mockItemLocalDataSource, times(1)).deleteItems(item)
    }

    /**
     * [ItemRepository.syncItems] Test´s
     * */
    @Test
    fun syncItemsTest() = runBlocking {
        val localLastItemsSynchronizationDate = Date(1618865396885)
        val remoteLastItemsSynchronizationDate = Date(1618865522292)
        val localDeletedItem1 = Item(localId = 6, 11, "Item 11 content", isSync = false, deleted = true)
        val localDeletedItem2 = Item(localId = 7, 10, "Item 10 content", isSync = false, deleted = true)
        val localDeletedItem3 = Item(localId = 8, 20, "Item 20 content", isSync = false, deleted = true)
        val localDeletedItems = listOf(localDeletedItem1, localDeletedItem2, localDeletedItem3)
        val notSyncedItem1 = Item(localId = 1, 2, "Item 1 content", isSync = false)
        val notSyncedItem2 = Item(localId = 2, 3, "Item 2 content", isSync = false)
        val notSyncedItem3 = Item(localId = 3, 4, "Item 3 content", isSync = false)
        val notSyncedItem4 = Item(localId = 4, null, "Item 4 content", isSync = false)
        val notSyncedItem5 = Item(localId = 5, null, "Item 5 content", isSync = false)
        val notSyncedLocalItems = listOf(notSyncedItem1, notSyncedItem2, notSyncedItem3, notSyncedItem4, notSyncedItem5)
        val expectedSyncedItem1 = Item(localId = 1, 2, "Item 1 content", isSync = true)
        val expectedSyncedItem2 = Item(localId = 2, 3, "Item 2 content", isSync = true)
        val expectedSyncedItem3 = Item(localId = 3, 4, "Item 3 content", isSync = true)
        val expectedSyncedItem4 = Item(localId = 4, 100, "Item 4 content", isSync = true)
        val expectedSyncedItem5 = Item(localId = 5, 101, "Item 5 content", isSync = true)

        val itemFromAPI1 = Item(localId = null, serverId = 50, "Item 50 content")
        val itemFromAPI2 = Item(localId = null, serverId = 60, "Item 50 content")
        val expectedItemFromAPI1toStore = Item(localId = null, serverId = 50, "Item 50 content", isSync = true, deleted = false)
        val expectedItemFromAPI2toStore = Item(localId = null, serverId = 60, "Item 50 content", isSync = true, deleted = false)
        val expectedGetItemApiResponse = ItemsApiResponse(
            listOf(
                itemFromAPI1,
                itemFromAPI2
            ),
            deletedItemsIds = listOf(2, 3),
            lastSyncDate = remoteLastItemsSynchronizationDate
        )

        `when`(mockItemLocalDataSource.deletedItems()).then { localDeletedItems }
        `when`(mockItemLocalDataSource.getLastItemsSynchronizationDate()).then { localLastItemsSynchronizationDate }
        `when`(mockItemRemoteDataSource.createItem(notSyncedItem4)).then { CreateItemApiResponse(100) }
        `when`(mockItemRemoteDataSource.createItem(notSyncedItem5)).then { CreateItemApiResponse(101) }
        `when`(mockItemLocalDataSource.notSynchronizedItems()).then { notSyncedLocalItems }
        `when`(mockItemRemoteDataSource.getItems(localLastItemsSynchronizationDate)).then { expectedGetItemApiResponse }

        itemRepository.syncItems()

        // verifies that locally deleted items are being synced
        verify(mockItemLocalDataSource, times(1)).deletedItems()
        verify(mockItemRemoteDataSource, times(1)).deleteItem(localDeletedItem1)
        verify(mockItemRemoteDataSource, times(1)).deleteItem(localDeletedItem2)
        verify(mockItemRemoteDataSource, times(1)).deleteItem(localDeletedItem3)
        verify(mockItemLocalDataSource, times(1)).deleteItems(localDeletedItem1)
        verify(mockItemLocalDataSource, times(1)).deleteItems(localDeletedItem2)
        verify(mockItemLocalDataSource, times(1)).deleteItems(localDeletedItem3)
        // verifies that items not synced locally are sent to the remote data source
        verify(mockItemLocalDataSource, times(1)).getLastItemsSynchronizationDate()
        verify(mockItemRemoteDataSource, times(1)).updateItem(notSyncedItem1)
        verify(mockItemRemoteDataSource, times(1)).updateItem(notSyncedItem2)
        verify(mockItemRemoteDataSource, times(1)).updateItem(notSyncedItem3)
        verify(mockItemRemoteDataSource, times(1)).createItem(notSyncedItem4)
        verify(mockItemRemoteDataSource, times(1)).createItem(notSyncedItem5)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedSyncedItem1)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedSyncedItem2)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedSyncedItem3)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedSyncedItem4)
        verify(mockItemLocalDataSource, times(1)).updateItems(expectedSyncedItem5)
        // verifies the obtaining of new data from the remote data source
        verify(mockItemLocalDataSource, times(1)).notSynchronizedItems()
        verify(mockItemLocalDataSource, times(1)).getLastItemsSynchronizationDate()
        verify(mockItemRemoteDataSource, times(1)).getItems(localLastItemsSynchronizationDate)
        verify(mockItemLocalDataSource, times(1)).storeLastItemsSynchronizationDate(remoteLastItemsSynchronizationDate)
        verify(mockItemLocalDataSource, times(1)).deleteItemsByServerId(2, 3)
        verify(mockItemLocalDataSource, times(1)).insertItems(expectedItemFromAPI1toStore, expectedItemFromAPI2toStore)
    }

    @Test
    fun getItemTest() = runBlocking {
        val expectedItem = Item(localId = 1, serverId = null, "Content")

        `when`(mockItemLocalDataSource.itemByLocalId(1)).then { expectedItem }

        val item = itemRepository.getItem(1)

        verify(mockItemLocalDataSource, times(1)).itemByLocalId(1)
        assert(item == expectedItem)
    }

    @Test
    fun allItemsTest() = runBlocking {
        val expectedItems = MutableLiveData<List<Item>>().apply {
            value = listOf(Item(localId = 1, serverId = null, "Content"))
        }

        `when`(mockItemLocalDataSource.allItemsLiveData()).then { expectedItems }

        val items = itemRepository.allItems()

        verify(mockItemLocalDataSource, times(1)).allItemsLiveData()
        assert(items == expectedItems)
    }
}
