package com.mmachado53.simplemvvmapp.data.local.dao.typeconverters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

open class BitmapConverter {
    @TypeConverter
    fun fromBitMapToByteArray(bitmap: Bitmap?): ByteArray? = bitmap?.let {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.toByteArray()
    }

    @TypeConverter
    fun fromByteArrayToBitmap(byteArray: ByteArray?): Bitmap? = byteArray?.let {
        BitmapFactory.decodeByteArray(it, 0, it.size)
    }
}
