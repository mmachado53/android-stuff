package com.mmachado53.simplemvvmapp.commons.extensions

import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun MaterialAlertDialogBuilder.setItems(
    itemsRes: List<Int>,
    listener: DialogInterface.OnClickListener?
): MaterialAlertDialogBuilder {
    val items = itemsRes.map {
        context.getString(it)
    }
    return this.setItems(items.toTypedArray(), listener)
}
