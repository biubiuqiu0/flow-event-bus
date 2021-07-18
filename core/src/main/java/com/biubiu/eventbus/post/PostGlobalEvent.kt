package com.biubiu.eventbus.post

import com.biubiu.eventbus.core.EventBusCore
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider


//_______________________________________
//          post app scope event
//_______________________________________

fun postGlobalEvent(eventName: String, value: Any) {
    postGlobalDelayEvent(eventName, value, 0L)
}

fun postGlobalDelayEvent(eventName: String, value: Any, timeMillis: Long) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .postEvent(eventName, value, timeMillis)
}


inline fun <reified T> postGlobalDelayEvent(event: T, timeMillis: Long) {
    postGlobalDelayEvent(T::class.java.name, event!!, timeMillis)
}

inline fun <reified T> postGlobalEvent(event: T) {
    postGlobalDelayEvent(event!!, 0L)
}


//_______________________________________
//          post app scope sticky event
//_______________________________________

fun postGlobalStickyEvent(eventName: String, value: Any) {
    postGlobalStickyDelayEvent(eventName, value, 0L)
}

fun postGlobalStickyDelayEvent(eventName: String, value: Any, timeMillis: Long) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .postStickyEvent(eventName, value, timeMillis)
}


inline fun <reified T> postGlobalStickyDelayEvent(event: T, timeMillis: Long) {
    postGlobalStickyDelayEvent(T::class.java.name, event!!, timeMillis)
}

inline fun <reified T> postGlobalStickyEvent(event: T) {
    postGlobalStickyEvent(T::class.java.name, event!!)
}
