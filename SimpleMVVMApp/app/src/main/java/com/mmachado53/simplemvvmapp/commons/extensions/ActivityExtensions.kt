package com.mmachado53.simplemvvmapp.commons.extensions

import android.app.Activity
import android.app.Dialog
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mmachado53.simplemvvmapp.R

fun Activity.showLoading() {
    if (getLoadingDialog()?.isShowing == true) {
        return
    }
    val dialog = MaterialAlertDialogBuilder(this)
        .setView(ProgressBar(this))
        .setCancelable(false)
        .create()
    dialog.show()
    window.decorView.setTag(R.string.TAG_LOADING_DIALOG, dialog)
}

fun Fragment.showLoading() = requireActivity().showLoading()

fun Activity.hideLoading() {
    getLoadingDialog()?.dismiss()
    window.decorView.setTag(R.string.TAG_LOADING_DIALOG, null)
}

fun Fragment.hideLoading() = requireActivity().hideLoading()

private fun Activity.getLoadingDialog(): Dialog? {
    return window.decorView.getTag(R.string.TAG_LOADING_DIALOG) as? Dialog
}
