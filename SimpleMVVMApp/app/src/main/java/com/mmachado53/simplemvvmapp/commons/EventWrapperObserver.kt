package com.mmachado53.simplemvvmapp.commons

import androidx.lifecycle.Observer

class EventWrapperObserver<T>(private val onEventUnhandledContent: (T) -> Unit) :
    Observer<EventWrapper<T>> {
    override fun onChanged(event: EventWrapper<T>?) {
        event?.getValueIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}
