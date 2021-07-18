package com.biubiu.eventbus.observe

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.biubiu.eventbus.core.EventBusCore
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

//_______________________________________
//          global scope event
//_______________________________________

inline fun <reified T> LifecycleOwner.observeGlobalEvent(
    noinline onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(T::class.java.name, Lifecycle.State.STARTED, onReceived)
}

inline fun <reified T> LifecycleOwner.observeGlobalEvent(
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(T::class.java.name, Lifecycle.State.STARTED, dispatcher, onReceived)
}

inline fun <reified T> LifecycleOwner.observeGlobalEvent(
    minActiveState: Lifecycle.State,
    noinline onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(T::class.java.name, minActiveState, onReceived)
}

inline fun <reified T> LifecycleOwner.observeGlobalEvent(
    minActiveState: Lifecycle.State,
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(T::class.java.name, minActiveState, dispatcher, onReceived)
}



//_______________________________________
//          global scope simple event
//_______________________________________

fun <T> LifecycleOwner.observeGlobalEvent(
    eventName: String,
    onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(eventName, Lifecycle.State.STARTED, onReceived)
}

fun <T> LifecycleOwner.observeGlobalEvent(
    eventName: String,
    minActiveState: Lifecycle.State,
    onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(eventName, minActiveState, Dispatchers.Main, onReceived)
}

fun <T> LifecycleOwner.observeGlobalEvent(
    eventName: String,
    dispatcher: CoroutineDispatcher,
    onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(eventName, Lifecycle.State.STARTED, dispatcher, onReceived)
}

fun <T> LifecycleOwner.observeGlobalEvent(
    eventName: String,
    minActiveState: Lifecycle.State,
    dispatcher: CoroutineDispatcher= Dispatchers.Main,
    onReceived: (T) -> Unit
) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .observeEvent(
            eventName,
            this,
            minActiveState,
            dispatcher,
            onReceived
        )
}
