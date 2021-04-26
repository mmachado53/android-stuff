package com.mmachado53.simplemvvmapp.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mmachado53.simplemvvmapp.R
import com.mmachado53.simplemvvmapp.commons.EventWrapperObserver
import com.mmachado53.simplemvvmapp.commons.extensions.hideLoading
import com.mmachado53.simplemvvmapp.commons.extensions.showLoading
import com.mmachado53.simplemvvmapp.commons.extensions.supportActionBar
import com.mmachado53.simplemvvmapp.databinding.FragmentItemFormBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ItemFormFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ItemFormViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ItemFormViewModel::class.java)
    }

    private lateinit var binding: FragmentItemFormBinding

    private val args: ItemFormFragmentArgs by navArgs()

    private val itemId: Long? by lazy {
        if (args.itemId >= 0) args.itemId else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId?.let { viewModel.loadItem(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemFormBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        handleTitle()
        setObservers()
        setupViews()
        return binding.root
    }

    private fun handleTitle() {
        val titleResId = if (itemId != null) R.string.fragment_item_form_edit_title
        else R.string.fragment_item_form_new_title
        supportActionBar?.setTitle(titleResId)
    }

    private fun setObservers() {
        viewModel.showLoading.observe(viewLifecycleOwner) {
            if (it) showLoading()
            else hideLoading()
        }

        viewModel.itemSavedEvent.observe(
            viewLifecycleOwner,
            EventWrapperObserver {
                if (it) findNavController().popBackStack()
            }
        )
    }

    private fun setupViews() {
        binding.cancelButton.setOnClickListener { findNavController().popBackStack() }
    }
}
