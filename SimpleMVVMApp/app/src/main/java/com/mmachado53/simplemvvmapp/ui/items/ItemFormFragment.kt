package com.mmachado53.simplemvvmapp.ui.items

import android.app.Activity
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mmachado53.simplemvvmapp.R
import com.mmachado53.simplemvvmapp.commons.EventWrapperObserver
import com.mmachado53.simplemvvmapp.commons.FileUtils
import com.mmachado53.simplemvvmapp.commons.IntentUtils
import com.mmachado53.simplemvvmapp.commons.extensions.buildActivityResultLauncher
import com.mmachado53.simplemvvmapp.commons.extensions.hideLoading
import com.mmachado53.simplemvvmapp.commons.extensions.newCacheFile
import com.mmachado53.simplemvvmapp.commons.extensions.setItems
import com.mmachado53.simplemvvmapp.commons.extensions.showLoading
import com.mmachado53.simplemvvmapp.commons.extensions.supportActionBar
import com.mmachado53.simplemvvmapp.databinding.FragmentItemFormBinding
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ItemFormFragment : DaggerFragment() {

    companion object {
        const val MAX_IMAGE_SIZE = 300
    }

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

    private val temporalFile: File by lazy {
        requireContext().newCacheFile("sample.jpg")
    }

    private val cameraActivityLauncher = buildActivityResultLauncher { activityResult ->
        // Handle when take a photo from the camera app
        if (activityResult.resultCode == Activity.RESULT_OK && temporalFile.exists()) {
            handleImageUri(temporalFile.toUri())
        }
    }

    private val galleryActivityLauncher = buildActivityResultLauncher { activityResult ->
        // Handle when selected a photo from the library
        if (activityResult.resultCode == Activity.RESULT_OK) {
            activityResult?.data?.data?.let { handleImageUri(it) }
        }
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
        binding.attachImageButton.setOnClickListener { showAttachImageOptions() }
        binding.removeImageButton.setOnClickListener { viewModel.itemImage.value = null }
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

    private fun showAttachImageOptions() {
        val options: List<Int> = listOf(
            R.string.fragment_item_form_attach_image_option_gallery,
            R.string.fragment_item_form_attach_image_option_camera
        )
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setItems(options) { _: DialogInterface?, which: Int ->
            when (options[which]) {
                R.string.fragment_item_form_attach_image_option_gallery -> openGallery()
                R.string.fragment_item_form_attach_image_option_camera -> openCamera()
            }
        }
        builder.setNegativeButton(R.string.fragment_item_form_attach_image_option_cancel, null)
        builder.show()
    }

    private fun openGallery() = galleryActivityLauncher.launch(IntentUtils.intentGallery())

    private fun openCamera() {
        val uri = FileProvider.getUriForFile(
            requireActivity(),
            getString(R.string.file_provider_authorities),
            temporalFile
        )
        cameraActivityLauncher.launch(IntentUtils.intentCamera(requireContext(), uri))
    }

    private fun handleImageUri(imageUri: Uri) {
        CoroutineScope(Dispatchers.Main).launch {
            FileUtils.decodeBitmapFromUri(requireContext(), imageUri, MAX_IMAGE_SIZE)?.let {
                viewModel.itemImage.postValue(it)
            }
        }
    }
}
