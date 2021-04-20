package com.mmachado53.simplemvvmapp.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mmachado53.simplemvvmapp.databinding.FragmentItemFormBinding

class ItemFormFragment : Fragment() {
    lateinit var binding: FragmentItemFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemFormBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }
}
