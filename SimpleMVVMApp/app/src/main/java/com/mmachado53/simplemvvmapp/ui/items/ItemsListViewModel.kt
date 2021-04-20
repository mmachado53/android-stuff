package com.mmachado53.simplemvvmapp.ui.items

import androidx.lifecycle.ViewModel
import com.mmachado53.simplemvvmapp.data.ItemRepositoryInterface
import javax.inject.Inject

class ItemsListViewModel @Inject constructor(private val repositoryInterface: ItemRepositoryInterface) : ViewModel()
