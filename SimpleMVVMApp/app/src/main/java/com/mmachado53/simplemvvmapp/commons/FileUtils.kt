package com.mmachado53.simplemvvmapp.commons

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.mmachado53.simplemvvmapp.commons.extensions.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FileUtils {

    suspend fun decodeBitmapFromUri(context: Context, photoUri: Uri, maxSize: Int? = null): Bitmap? {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            try {
                contentResolver.openInputStream(photoUri).use {
                    val bitmap = BitmapFactory.decodeStream(it)
                    maxSize?.let { maxSize ->
                        val resizedImage = bitmap.scale(maxSize)
                        bitmap.recycle()
                        return@withContext resizedImage
                    } ?: kotlin.run {
                        return@withContext bitmap
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext null
        }
    }
}
