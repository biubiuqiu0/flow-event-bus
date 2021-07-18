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
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
) {
    this.observeGlobalEvent(T::class.java.name, dispatcher, minActiveState, isSticky, onReceived)
}


//_______________________________________
//          global scope simple event
//_______________________________________

fun <T> LifecycleOwner.observeGlobalEvent(
    eventName: String,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    onReceived: (T) -> Unit
) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .observeEvent(
            this,
            eventName,
            minActiveState,
            dispatcher,
            isSticky,
            onReceived
        )
}
