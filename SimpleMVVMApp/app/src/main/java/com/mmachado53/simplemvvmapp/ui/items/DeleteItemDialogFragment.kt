package com.mmachado53.simplemvvmapp.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mmachado53.simplemvvmapp.databinding.DialogFragmentDeleteItemBinding

class DeleteItemDialogFragment : DialogFragment() {

    lateinit var binding: DialogFragmentDeleteItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentDeleteItemBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }
}
