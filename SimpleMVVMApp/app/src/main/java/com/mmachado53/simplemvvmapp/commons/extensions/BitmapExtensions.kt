package com.mmachado53.simplemvvmapp.commons.extensions

import android.graphics.Bitmap
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Bitmap.scale(maxSize: Int) = withContext(Dispatchers.Main) {
    val largeSize = width.coerceAtLeast(height)
    var scale = maxSize.toFloat() / largeSize.toFloat()
    scale = if (scale > 1) 1F else scale
    val newWidth = (width.toFloat() * scale).toInt()
    val newHeight = (height.toFloat() * scale).toInt()
    return@withContext scale(newWidth, newHeight)
}
