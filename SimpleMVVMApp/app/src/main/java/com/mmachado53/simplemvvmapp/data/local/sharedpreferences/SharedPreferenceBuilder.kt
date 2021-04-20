package com.mmachado53.simplemvvmapp.data.local.sharedpreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPreferenceBuilder {
    companion object {
        private const val DATABASE_NAME = "SimpleMVVMAppSharedPreferences"
        fun build(context: Context): SharedPreferences = context.getSharedPreferences(DATABASE_NAME, MODE_PRIVATE)
    }
}
