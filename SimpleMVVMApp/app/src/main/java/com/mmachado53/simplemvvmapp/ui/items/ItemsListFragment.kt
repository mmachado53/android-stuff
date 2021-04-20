package com.mmachado53.simplemvvmapp.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mmachado53.simplemvvmapp.databinding.FragmentItemsListBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ItemsListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ItemsListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ItemsListViewModel::class.java)
    }

    lateinit var binding: FragmentItemsListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemsListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}
