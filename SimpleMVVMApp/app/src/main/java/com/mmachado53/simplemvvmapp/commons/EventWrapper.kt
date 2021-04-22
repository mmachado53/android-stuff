package com.mmachado53.simplemvvmapp.commons

class EventWrapper<out T>(private val value: T) {

    var hasBeenHandled = false
        private set

    fun getValueIfNotHandled(): T? {
        if (hasBeenHandled) {
            return null
        }
        hasBeenHandled = true
        return value
    }

    fun getValue(): T = value
}
