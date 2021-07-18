package com.biubiu.eventbus.observe

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.biubiu.eventbus.core.EventBusCore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


//_______________________________________
//          Activity scope event observe
//_______________________________________


inline fun <reified T> ComponentActivity.observeEvent(
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(Lifecycle.State.STARTED, onReceived)
}

inline fun <reified T> ComponentActivity.observeEvent(
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(Lifecycle.State.STARTED, dispatcher, onReceived)
}

inline fun <reified T> ComponentActivity.observeEvent(
    minActiveState: Lifecycle.State,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(minActiveState, Dispatchers.Main, onReceived)
}

inline fun <reified T> ComponentActivity.observeEvent(
    minActiveState: Lifecycle.State,
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    ViewModelProvider(this).get(EventBusCore::class.java)
        .observeEvent(
            T::class.java.name,
            this,
            minActiveState,
            dispatcher,
            onReceived
        )
}



//_______________________________________
//          fragment observe Activity scope event
//_______________________________________


inline fun <reified T> Fragment.observeActivityEvent(
    noinline onReceived: (T) -> Unit
) {
    this.observeActivityEvent(Lifecycle.State.STARTED, onReceived)
}

inline fun <reified T> Fragment.observeActivityEvent(
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    this.observeActivityEvent(Lifecycle.State.STARTED, dispatcher, onReceived)
}

inline fun <reified T> Fragment.observeActivityEvent(
    minActiveState: Lifecycle.State,
    noinline onReceived: (T) -> Unit
) {
    this.observeActivityEvent(minActiveState, Dispatchers.Main, onReceived)
}

inline fun <reified T> Fragment.observeActivityEvent(
    minActiveState: Lifecycle.State,
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    ViewModelProvider(requireActivity()).get(EventBusCore::class.java)
        .observeEvent(
            T::class.java.name,
            this,
            minActiveState,
            dispatcher,
            onReceived
        )
}
