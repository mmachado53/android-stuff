package com.mmachado53.simplemvvmapp.commons

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

object IntentUtils {
    fun intentGallery(): Intent {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        return Intent.createChooser(intent, null)
    }

    fun intentCamera(context: Context, photoUri: Uri): Intent {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity(context.packageManager)?.also {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        return takePictureIntent
    }
}
