package com.biubiu.eventbus.observe

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher


inline fun <reified T> LifecycleOwner.observeEvent(
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(T::class.java.name, Lifecycle.State.STARTED, onReceived)
}

inline fun <reified T> LifecycleOwner.observeEvent(
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(T::class.java.name, Lifecycle.State.STARTED, dispatcher, onReceived)
}

inline fun <reified T> LifecycleOwner.observeEvent(
    minActiveState: Lifecycle.State,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(T::class.java.name, minActiveState, onReceived)
}

inline fun <reified T> LifecycleOwner.observeEvent(
    minActiveState: Lifecycle.State,
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(T::class.java.name, minActiveState, dispatcher, onReceived)
}
