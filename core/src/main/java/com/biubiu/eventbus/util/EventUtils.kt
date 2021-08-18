package com.biubiu.eventbus.util

import androidx.lifecycle.*
import com.biubiu.eventbus.core.EventBusCore
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun <reified T> getEventObserverCount(event: Class<T>): Int {
    return ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .getEventObserverCount(event.name)
}

inline fun <reified T> getEventObserverCount(scope: ViewModelStoreOwner, event: Class<T>): Int {
    return ViewModelProvider(scope).get(EventBusCore::class.java)
        .getEventObserverCount(event.name)
}


//移除事件
inline fun <reified T> removeStickyEvent(event: Class<T>) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .removeStickEvent(event.name)
}

inline fun <reified T> removeStickyEvent(scope: ViewModelStoreOwner, event: Class<T>) {
    ViewModelProvider(scope).get(EventBusCore::class.java)
        .removeStickEvent(event.name)
}


// 清除事件缓存
inline fun <reified T> clearStickyEvent(event: Class<T>) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .clearStickEvent(event.name)
}

inline fun <reified T> clearStickyEvent(scope: ViewModelStoreOwner, event: Class<T>) {
    ViewModelProvider(scope).get(EventBusCore::class.java)
        .clearStickEvent(event.name)
}



fun <T> LifecycleOwner.launchWhenStateAtLeast(
    minState: Lifecycle.State,
    block: suspend CoroutineScope.() -> T
) {
    lifecycleScope.launch {
        lifecycle.whenStateAtLeast(minState, block)
    }
}
