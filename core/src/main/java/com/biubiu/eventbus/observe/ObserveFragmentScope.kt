package com.biubiu.eventbus.observe

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.biubiu.eventbus.core.EventBusCore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

//_______________________________________
//          fragment scope event observe
//_______________________________________

inline fun <reified T> Fragment.observeEvent(
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(Lifecycle.State.STARTED, onReceived)
}

inline fun <reified T> Fragment.observeEvent(
    dispatcher: CoroutineDispatcher,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(Lifecycle.State.STARTED, dispatcher, onReceived)
}

inline fun <reified T> Fragment.observeEvent(
    minActiveState: Lifecycle.State,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(minActiveState, Dispatchers.Main, onReceived)
}

inline fun <reified T> Fragment.observeEvent(
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

