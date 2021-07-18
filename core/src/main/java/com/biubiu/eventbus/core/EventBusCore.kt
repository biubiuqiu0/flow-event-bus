package com.biubiu.eventbus.core

import androidx.lifecycle.*
import com.biubiu.eventbus.EventBusInitializer
import com.biubiu.eventbus.util.launchWhenStateAtLeast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.logging.Level
import kotlin.coroutines.EmptyCoroutineContext

class EventBusCore : ViewModel() {

    //正常事件
    private val eventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    //粘性事件
    private val stickyEventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    private fun getEventFlow(eventName: String, isSticky: Boolean): MutableSharedFlow<Any> {
        return if (isSticky) {
            stickyEventFlows[eventName]
        } else {
            eventFlows[eventName]
        } ?: MutableSharedFlow<Any>(
            replay = if (isSticky) 1 else 0,
            extraBufferCapacity = Int.MAX_VALUE
        ).also {
            if (isSticky) {
                stickyEventFlows[eventName] = it
            } else {
                eventFlows[eventName] = it
            }
        }
    }

    fun <T : Any> observeEvent(
        eventName: String,
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State,
        dispatcher: CoroutineDispatcher,
        onReceived: (T) -> Unit
    ) {
        EventBusInitializer.logger?.log(Level.WARNING, "observe Event:$eventName")
        collectEventFlow(
            lifecycleOwner,
            minState,
            dispatcher,
            getEventFlow(eventName, false),
            onReceived
        )
    }

    fun <T : Any> observeStickEvent(
        eventName: String,
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State,
        dispatcher: CoroutineDispatcher,
        onReceived: (T) -> Unit
    ) {
        EventBusInitializer.logger?.log(Level.WARNING, "observe Event:$eventName")
        collectEventFlow(
            lifecycleOwner,
            minState,
            dispatcher,
            getEventFlow(eventName, true),
            onReceived
        )
    }

    private fun <T> collectEventFlow(
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        flow: MutableSharedFlow<Any>,
        onReceived: (T) -> Unit
    ) {
        lifecycleOwner.launchWhenStateAtLeast(minState) {
            flow.collect { value ->
                this.launch(dispatcher ?: EmptyCoroutineContext) {
                    try {
                        onReceived.invoke(value as T)
                    } catch (e: ClassCastException) {
                        EventBusInitializer.logger?.log(
                            Level.WARNING,
                            "class cast error on message received: $value",
                            e
                        )
                    } catch (e: Exception) {
                        EventBusInitializer.logger?.log(
                            Level.WARNING,
                            "error on message received: $value",
                            e
                        )
                    }
                }
            }
        }
    }


    fun postEvent(eventName: String, value: Any, timeMillis: Long) {
        EventBusInitializer.logger?.log(Level.WARNING, "post Event:$eventName")

        getEventFlow(eventName, false).let { flow ->
            viewModelScope.launch {
                delay(timeMillis)
                flow.emit(value)
            }
        }
    }

    fun postStickyEvent(eventName: String, value: Any, timeMillis: Long) {
        EventBusInitializer.logger?.log(Level.WARNING, "post Event:$eventName  isSticky")

        getEventFlow(eventName, true).let { flow ->
            viewModelScope.launch {
                delay(timeMillis)
                flow.emit(value)
            }
        }
    }
}