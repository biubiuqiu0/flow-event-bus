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
