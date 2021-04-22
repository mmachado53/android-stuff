package com.mmachado53.simplemvvmapp.ui.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmachado53.simplemvvmapp.commons.EventWrapper
import com.mmachado53.simplemvvmapp.commons.exceptions.RemoteAPIException
import com.mmachado53.simplemvvmapp.data.ItemRepositoryInterface
import com.mmachado53.simplemvvmapp.data.model.Item
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemsListViewModel @Inject constructor(private val repository: ItemRepositoryInterface) : ViewModel() {

    val allItems: LiveData<List<Item>> = repository.allItems()

    val showNoDataMessage = Transformations.map(allItems) {
        it.isEmpty()
    }

    val showConnectionErrorMessage: LiveData<EventWrapper<RemoteAPIException?>> = Transformations.map(repository.currentRemoteApiException()) {
        EventWrapper(it)
    }

    private val _showLoading = MutableLiveData<Boolean>(false)

    val showLoading: LiveData<Boolean> = _showLoading

    fun loadData() {
        viewModelScope.launch {
            _showLoading.value = true
            repository.syncItems()
            _showLoading.value = false
        }
    }
}
