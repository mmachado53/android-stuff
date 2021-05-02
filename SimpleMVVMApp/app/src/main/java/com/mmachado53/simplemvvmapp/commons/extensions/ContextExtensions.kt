package com.mmachado53.simplemvvmapp.commons.extensions

import android.content.Context
import java.io.File

fun Context.newCacheFile(fileName: String) = File(cacheDir, fileName)
