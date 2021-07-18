package com.biubiu.eventbus.post

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.biubiu.eventbus.core.EventBusCore
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider


inline fun <reified T> removeStickyEvent(event: Class<T>) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .removeStickEvent(event.name)
}


inline fun <reified T> removeStickyEvent(scope: Fragment, event: Class<T>) {
    ViewModelProvider(scope).get(EventBusCore::class.java)
        .removeStickEvent(event.name)
}


inline fun <reified T> removeStickyEvent(scope: ComponentActivity, event: Class<T>) {
    ViewModelProvider(scope).get(EventBusCore::class.java)
        .removeStickEvent(event.name)
}
