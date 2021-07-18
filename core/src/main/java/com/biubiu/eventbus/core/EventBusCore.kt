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
        lifecycleOwner: LifecycleOwner,
        eventName: String,
        minState: Lifecycle.State,
        dispatcher: CoroutineDispatcher,
        isSticky: Boolean,
        onReceived: (T) -> Unit
    ) {
        EventBusInitializer.logger?.log(Level.WARNING, "observe Event:$eventName")
        lifecycleOwner.launchWhenStateAtLeast(minState) {
            getEventFlow(eventName, isSticky).collect { value ->
                this.launch(dispatcher) {
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
        listOfNotNull(
            getEventFlow(eventName, false),
            getEventFlow(eventName, true)
        ).forEach { flow ->
            viewModelScope.launch {
                delay(timeMillis)
                flow.emit(value)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun removeStickEvent(eventName: String) {
        stickyEventFlows[eventName]?.resetReplayCache()
    }
}