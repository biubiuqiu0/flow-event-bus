package com.biubiu.eventbus.observe

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.biubiu.eventbus.core.EventBusViewModel
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

fun <T> LifecycleOwner.observeEvent(
    eventName: String,
    onReceived: (T) -> Unit
) {
    this.observeEvent(eventName, Lifecycle.State.STARTED, onReceived)
}

fun <T> LifecycleOwner.observeEvent(
    eventName: String,
    minActiveState: Lifecycle.State,
    onReceived: (T) -> Unit
) {
    this.observeEvent(eventName, minActiveState,Dispatchers.Main, onReceived)
}

fun <T> LifecycleOwner.observeEvent(
    eventName: String,
    dispatcher: CoroutineDispatcher,
    onReceived: (T) -> Unit
) {
    this.observeEvent(eventName, Lifecycle.State.STARTED, dispatcher, onReceived)
}

fun <T> LifecycleOwner.observeEvent(
    eventName: String,
    minActiveState: Lifecycle.State,
    dispatcher: CoroutineDispatcher= Dispatchers.Main,
    onReceived: (T) -> Unit
) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusViewModel::class.java)
        .observeEvent(
            eventName,
            this,
            minActiveState,
            dispatcher,
            onReceived
        )
}
