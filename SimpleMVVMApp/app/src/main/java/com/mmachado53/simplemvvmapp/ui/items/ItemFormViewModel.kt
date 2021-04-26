package com.mmachado53.simplemvvmapp.ui.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmachado53.simplemvvmapp.commons.EventWrapper
import com.mmachado53.simplemvvmapp.data.ItemRepositoryInterface
import com.mmachado53.simplemvvmapp.data.model.Item
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemFormViewModel @Inject constructor(private val repository: ItemRepositoryInterface) : ViewModel() {

    private val _showLoading = MutableLiveData(false)

    val showLoading: LiveData<Boolean> = _showLoading

    private var itemToEdit: Item? = null

    val itemContent = MutableLiveData<String>()

    val saveButtonIsEnabled: LiveData<Boolean> = Transformations.map(itemContent) {
        formIsValid()
    }

    private val _itemSavedEvent = MutableLiveData<EventWrapper<Boolean>>()

    val itemSavedEvent: LiveData<EventWrapper<Boolean>> = _itemSavedEvent

    fun loadItem(itemId: Long) {
        viewModelScope.launch {
            repository.getItem(itemId)?.let {
                itemToEdit = it
                itemContent.value = it.content
            }
        }
    }

    fun saveItem() {
        if (formIsValid()) {
            viewModelScope.launch {
                _showLoading.value = true
                val itemToSave = itemToEdit?.let {
                    it.content = itemContent.value!!
                    return@let it
                } ?: kotlin.run {
                    return@run Item(localId = null, serverId = null, itemContent.value!!)
                }
                repository.insertUpdateItem(itemToSave)
                _showLoading.value = false
                _itemSavedEvent.value = EventWrapper(true)
            }
        }
    }

    private fun formIsValid(): Boolean = itemContent.value?.isNotBlank() ?: false
}
