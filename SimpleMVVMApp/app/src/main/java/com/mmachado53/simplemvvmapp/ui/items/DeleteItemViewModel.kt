package com.mmachado53.simplemvvmapp.ui.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmachado53.simplemvvmapp.commons.EventWrapper
import com.mmachado53.simplemvvmapp.data.ItemRepositoryInterface
import com.mmachado53.simplemvvmapp.data.model.Item
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteItemViewModel @Inject constructor(private val repository: ItemRepositoryInterface) : ViewModel() {

    private val _itemToDelete = MutableLiveData<Item>()

    val itemToDelete: LiveData<Item> = _itemToDelete

    private val _itemDeletedEvent = MutableLiveData<EventWrapper<Boolean>>()

    val itemDeletedEvent: LiveData<EventWrapper<Boolean>> = _itemDeletedEvent

    fun loadItemInfo(itemId: Long) {
        viewModelScope.launch {
            repository.getItem(itemId)?.let {
                _itemToDelete.value = it
            }
        }
    }

    fun delete() {
        itemToDelete.value?.let {
            viewModelScope.launch {
                repository.removeItem(it)
                _itemDeletedEvent.value = EventWrapper(true)
            }
        }
    }
}
