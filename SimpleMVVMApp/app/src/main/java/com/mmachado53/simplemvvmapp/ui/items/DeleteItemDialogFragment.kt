package com.mmachado53.simplemvvmapp.ui.items

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mmachado53.simplemvvmapp.commons.EventWrapperObserver
import com.mmachado53.simplemvvmapp.databinding.DialogFragmentDeleteItemBinding
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class DeleteItemDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DeleteItemViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DeleteItemViewModel::class.java)
    }

    private lateinit var binding: DialogFragmentDeleteItemBinding

    private val navArgs: DeleteItemDialogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadItemInfo(navArgs.itemId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentDeleteItemBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.cancelButton.setOnClickListener { findNavController().popBackStack() }
        setObservers()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    private fun setObservers() {
        viewModel.itemDeletedEvent.observe(
            viewLifecycleOwner,
            EventWrapperObserver {
                if (it) dismiss()
            }
        )
    }
}
