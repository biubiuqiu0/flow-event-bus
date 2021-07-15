package com.biubiu.eventbus.post

import com.biubiu.eventbus.core.EventBusViewModel
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider

//_______________________________________
//          post event
//_______________________________________

fun postDelayEvent(eventName: String, value: Any, timeMillis: Long) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusViewModel::class.java)
        .postEvent(eventName, value, false, timeMillis)
}

inline fun <reified T> postDelayEvent(event: T, timeMillis: Long) {
    postDelayEvent(T::class.java.name, event!!, timeMillis)
}

fun postEvent(eventName: String, value: Any) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusViewModel::class.java)
        .postEvent(eventName, value, false, 0L)
}

inline fun <reified T> postEvent(event: T) {
    postEvent(T::class.java.name, event!!)
}


//_______________________________________
//          post sticky event
//_______________________________________

fun postStickyDelayEvent(eventName: String, value: Any, timeMillis: Long) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusViewModel::class.java)
        .postStickyEvent(eventName, value, timeMillis)
}

inline fun <reified T> postStickyDelayEvent(event: T, timeMillis: Long) {
    postStickyDelayEvent(T::class.java.name, event!!, timeMillis)
}

fun postStickyEvent(eventName: String, value: Any) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusViewModel::class.java)
        .postStickyEvent(eventName, value,0L)
}

inline fun <reified T> postStickyEvent(event: T) {
    postStickyEvent(T::class.java.name, event!!)
}
