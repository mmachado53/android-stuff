package com.mmachado53.simplemvvmapp.commons

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun visible(view: View, visible: Boolean?) {
    view.visibility = if (visible == true) {
        View.VISIBLE
    } else View.GONE
}
