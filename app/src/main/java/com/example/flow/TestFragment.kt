package com.example.flow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.biubiu.eventbus.observe.observeActivityEvent
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.observe.observeGlobalEvent
import com.biubiu.eventbus.post.*
import com.biubiu.eventbus.post.postDelayEvent
import com.example.flow.event.ActivityEvent
import com.example.flow.event.FragmentEvent
import com.example.flow.event.GlobalEvent
import kotlinx.coroutines.Dispatchers

class TestFragment : Fragment(R.layout.fragment_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_fragment).setOnClickListener {
            postEvent(FragmentEvent("来自 Fragment"))
        }

        view.findViewById<Button>(R.id.button_app).setOnClickListener {
            postGlobalEvent(GlobalEvent("来自 Fragment"))
        }

        view.findViewById<Button>(R.id.button_activity).setOnClickListener {
            postActivityEvent(ActivityEvent("来自 Fragment"))
            postActivityDelayEvent(ActivityEvent("来自 Fragment"), 1000)
        }

        //监听全局事件
        observeGlobalEvent<GlobalEvent> {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent :${it.name}")
        }
        //监听Activity事件
        observeActivityEvent<ActivityEvent>(Dispatchers.IO) {
            Log.d(MainActivity.TAG, "TestFragment received ActivityEvent :${it.name}")
        }
        //监听自己Scope事件
        observeEvent<FragmentEvent>(Dispatchers.IO) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent :${it.name}")
        }

    }
}