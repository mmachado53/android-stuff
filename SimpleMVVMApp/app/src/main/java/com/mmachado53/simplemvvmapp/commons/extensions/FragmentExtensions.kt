package com.mmachado53.simplemvvmapp.commons.extensions

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

val Fragment.supportActionBar: ActionBar?
    get() = (requireActivity() as? AppCompatActivity)?.supportActionBar
