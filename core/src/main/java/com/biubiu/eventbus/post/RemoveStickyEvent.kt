package com.biubiu.eventbus.post

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.biubiu.eventbus.core.EventBusCore
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.reflect.KClass


fun removeGlobalStickyEvent(eventName: String) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .removeStickEvent(eventName)
}


inline fun <reified T> removeGlobalStickyEvent(event: Class<T>) {
    removeGlobalStickyEvent(event.name)
}


fun ViewModelStoreOwner.removeStickyEvent(eventName: String) {
    ViewModelProvider(this).get(EventBusCore::class.java)
        .removeStickEvent(eventName)
}


inline fun <reified T> ViewModelStoreOwner.removeStickyEvent(event: Class<T>) {
    removeStickyEvent(event.name)
}


fun Fragment.removeActivityStickyEvent(eventName: String) {
    ViewModelProvider(requireActivity()).get(EventBusCore::class.java)
        .removeStickEvent(eventName)
}

inline fun <reified T> Fragment.removeActivityStickyEvent(event: Class<T>) {
    removeActivityStickyEvent(event.name)
}