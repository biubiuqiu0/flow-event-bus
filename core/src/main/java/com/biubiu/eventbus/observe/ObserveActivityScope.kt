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
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
) {
    observeEvent(
        T::class.java.name,
        dispatcher,
        minActiveState,
        isSticky,
        onReceived
    )
}


fun <T> ComponentActivity.observeEvent(
    eventName: String,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    onReceived: (T) -> Unit
) {
    ViewModelProvider(this).get(EventBusCore::class.java)
        .observeEvent(
            this,
            eventName,
            minActiveState,
            dispatcher,
            isSticky,
            onReceived
        )
}


//_______________________________________
//          fragment observe Activity scope event
//_______________________________________
inline fun <reified T> Fragment.observeActivityEvent(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
) {
    observeActivityEvent(
        T::class.java.name,
        dispatcher,
        minActiveState,
        isSticky,
        onReceived
    )
}


fun <T> Fragment.observeActivityEvent(
    eventName: String,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    onReceived: (T) -> Unit
) {
    ViewModelProvider(requireActivity()).get(EventBusCore::class.java)
        .observeEvent(
            this,
            eventName,
            minActiveState,
            dispatcher,
            isSticky,
            onReceived
        )
}