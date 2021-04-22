package com.mmachado53.simplemvvmapp.commons

interface OnSelectWithAction<T, A : Enum<*>> {
    fun onSelect(item: T, action: A)
}
