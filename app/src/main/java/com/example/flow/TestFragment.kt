package com.example.flow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.biubiu.eventbus.observe.observeActivityEvent
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.observe.observeGlobalEvent
import com.biubiu.eventbus.post.*
import com.example.flow.event.ActivityEvent
import com.example.flow.event.FragmentEvent
import com.example.flow.event.GlobalEvent
import kotlinx.coroutines.Dispatchers

class TestFragment : Fragment(R.layout.fragment_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.button_app).setOnClickListener {
            observeActivityEvent<ActivityEvent>(isSticky = true) {
                Log.d(MainActivity.TAG, "TestFragment received FragmentEvent sticky  :${it.name}")
            }
        }

        view.findViewById<Button>(R.id.button_activity).setOnClickListener {
            removeActivityStickyEvent(ActivityEvent::class.java)
        }

        view.findViewById<Button>(R.id.button_fragment).setOnClickListener {
            observeActivityEvent<ActivityEvent>(isSticky = true) {
                Log.d(MainActivity.TAG, "TestFragment received FragmentEvent sticky  :${it.name}")
            }
        }


        //监听全局事件
        observeGlobalEvent<GlobalEvent> {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent :${it.name}")
        }
        observeGlobalEvent<GlobalEvent>(isSticky = true) {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent :${it.name}")
        }

        observeGlobalEvent<GlobalEvent>(dispatcher = Dispatchers.IO) {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent :${it.name}")
        }
        observeGlobalEvent<GlobalEvent>(minActiveState = Lifecycle.State.STARTED) {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent :${it.name}")
        }

        observeGlobalEvent<GlobalEvent>(
            minActiveState = Lifecycle.State.STARTED,
            dispatcher = Dispatchers.IO
        ) {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent :${it.name}")
        }


        //监听Activity事件

//        observeActivityEvent<ActivityEvent>(isSticky = true) {
//            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 2:${it.name}")
//        }
//
//        observeActivityEvent<ActivityEvent>(minActiveState = Lifecycle.State.STARTED) {
//            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 3:${it.name}")
//        }
//
//        observeActivityEvent<ActivityEvent>(Dispatchers.IO) {
//            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 4:${it.name}")
//        }
//
//        observeActivityEvent<ActivityEvent>(Dispatchers.IO, Lifecycle.State.STARTED) {
//            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 5:${it.name}")
//        }

        //监听自己Scope事件
        observeEvent<FragmentEvent>(Dispatchers.IO, Lifecycle.State.STARTED) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent :${it.name}")
        }

        observeEvent<FragmentEvent>(minActiveState = Lifecycle.State.STARTED) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent :${it.name}")
        }

        observeEvent<FragmentEvent>(Dispatchers.IO) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent :${it.name}")
        }

        observeEvent<FragmentEvent>(isSticky = true) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent :${it.name}")
        }

        observeEvent<FragmentEvent> {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent :${it.name}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}