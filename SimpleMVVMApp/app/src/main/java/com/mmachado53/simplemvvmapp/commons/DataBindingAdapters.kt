package com.mmachado53.simplemvvmapp.commons

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun visible(view: View, visible: Boolean?) {
    view.visibility = if (visible == true) {
        View.VISIBLE
    } else View.GONE
}

/**
 * [ImageView] Bindings Adapters
 */

@BindingAdapter("bitmap")
fun bitmap(imageView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imageView.setImageBitmap(bitmap)
    } ?: run {
        imageView.setImageResource(android.R.color.transparent)
    }
}
