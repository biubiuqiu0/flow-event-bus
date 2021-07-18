package com.biubiu.eventbus.post

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.biubiu.eventbus.core.EventBusCore

//单页面通信 - Activity/Fragment

//_______________________________________
//          post event
//_______________________________________
fun ViewModelStoreOwner.postEvent(eventName: String, value: Any) {
    postDelayEvent(eventName, value, 0L)
}

fun ViewModelStoreOwner.postDelayEvent(eventName: String, value: Any, timeMillis: Long) {
    ViewModelProvider(this).get(EventBusCore::class.java)
        .postEvent(eventName, value, timeMillis)
}

inline fun <reified T> ViewModelStoreOwner.postEvent(event: T) {
    postEvent(T::class.java.name, event!!)
}

inline fun <reified T> ViewModelStoreOwner.postDelayEvent(event: T, timeMillis: Long) {
    postDelayEvent(T::class.java.name, event!!, timeMillis)
}
//_______________________________________
//          post sticky event
//_______________________________________

fun ViewModelStoreOwner.postStickyDelayEvent(eventName: String, value: Any, timeMillis: Long) {
    ViewModelProvider(this).get(EventBusCore::class.java)
        .postStickyEvent(eventName, value, timeMillis)
}

fun ViewModelStoreOwner.postStickyEvent(eventName: String, value: Any) {
    postStickyDelayEvent(eventName, value, 0L)
}

inline fun <reified T> ViewModelStoreOwner.postStickyDelayEvent(event: T, timeMillis: Long) {
    postStickyDelayEvent(T::class.java.name, event!!, timeMillis)
}


inline fun <reified T> ViewModelStoreOwner.postStickyEvent(event: T) {
    postStickyDelayEvent(T::class.java.name, event!!, 0L)
}


//_______________________________________
//        fragment  post activity event
//_______________________________________
fun Fragment.postActivityEvent(eventName: String, value: Any) {
    postActivityDelayEvent(eventName, value, 0L)
}

fun Fragment.postActivityDelayEvent(eventName: String, value: Any, timeMillis: Long) {
    ViewModelProvider(requireActivity()).get(EventBusCore::class.java)
        .postEvent(eventName, value, timeMillis)
}

inline fun <reified T> Fragment.postActivityEvent(event: T) {
    postActivityDelayEvent(T::class.java.name, event!!, 0L)
}

inline fun <reified T> Fragment.postActivityDelayEvent(event: T, timeMillis: Long) {
    postActivityDelayEvent(T::class.java.name, event!!, timeMillis)
}
//_______________________________________
//          post sticky event
//_______________________________________

fun Fragment.postActivityStickyEvent(eventName: String, value: Any, timeMillis: Long) {
    ViewModelProvider(this).get(EventBusCore::class.java)
        .postStickyEvent(eventName, value, timeMillis)
}

fun Fragment.postActivityStickyEvent(eventName: String, value: Any) {
    postActivityStickyEvent(eventName, value, 0L)
}

inline fun <reified T> Fragment.postActivityStickyDelayEvent(event: T, timeMillis: Long) {
    postActivityStickyEvent(T::class.java.name, event!!, timeMillis)
}


inline fun <reified T> Fragment.postActivityStickyEvent(event: T) {
    postActivityStickyDelayEvent(event, 0L)
}