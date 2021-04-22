package com.mmachado53.simplemvvmapp.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mmachado53.simplemvvmapp.R
import com.mmachado53.simplemvvmapp.commons.EventWrapperObserver
import com.mmachado53.simplemvvmapp.commons.OnSelectWithAction
import com.mmachado53.simplemvvmapp.data.model.Item
import com.mmachado53.simplemvvmapp.databinding.FragmentItemsListBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ItemsListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ItemsListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ItemsListViewModel::class.java)
    }

    private lateinit var binding: FragmentItemsListBinding

    private val itemsAdapter: ItemsListAdapter by lazy {
        ItemsListAdapter(
            object : OnSelectWithAction<Item, ItemsListAdapter.SelectAction> {
                override fun onSelect(item: Item, action: ItemsListAdapter.SelectAction) {
                    when (action) {
                        ItemsListAdapter.SelectAction.Show -> navigateToEditItem(item)
                        ItemsListAdapter.SelectAction.Delete -> navigateToRemoveItem(item)
                    }
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.items_list_menu, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_item -> {
                findNavController().navigate(R.id.action_itemsListFragment_to_itemFormFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemsListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupViews()
        setObservers()
        return binding.root
    }

    private fun setupViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = itemsAdapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadData()
        }
    }

    private fun setObservers() {
        viewModel.allItems.observe(viewLifecycleOwner) {
            itemsAdapter.submitList(it)
        }
        viewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
            binding.swipeRefreshLayout.isRefreshing = showLoading
        }
        viewModel.showConnectionErrorMessage.observe(
            viewLifecycleOwner,
            EventWrapperObserver { apiException ->
                apiException?.let {
                    Snackbar.make(
                        requireView(),
                        R.string.fragment_items_list_generic_connection_error,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun navigateToEditItem(item: Item) {
        val direction = ItemsListFragmentDirections.actionItemsListFragmentToItemFormFragment(itemId = item.localId!!)
        findNavController().navigate(direction)
    }

    private fun navigateToRemoveItem(item: Item) {
        val direction = ItemsListFragmentDirections.actionItemsListFragmentToDeleteItemDialogFragment(itemId = item.localId!!)
        findNavController().navigate(direction)
    }
}
