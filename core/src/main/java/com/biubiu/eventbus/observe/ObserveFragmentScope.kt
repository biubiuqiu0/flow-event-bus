package com.biubiu.eventbus.observe

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.biubiu.eventbus.core.EventBusCore
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

//_______________________________________
//          fragment scope event observe
//_______________________________________

inline fun <reified T> Fragment.observeEvent(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
) {
    this.observeEvent(
        T::class.java.name,
        dispatcher,
        minActiveState,
        isSticky,
        onReceived
    )
}


fun <T> Fragment.observeEvent(
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
