package com.example.flow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.post.postEvent
import com.example.flow.event.ActivityEvent
import com.example.flow.event.CustomComponent
import com.example.flow.event.FragmentEvent
import com.example.flow.event.GlobalEvent
import kotlinx.coroutines.Dispatchers

class TestFragment : Fragment(R.layout.fragment_test) {

    val test: CustomComponent by lazy {
        CustomComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        test.observer(this)

        view.findViewById<Button>(R.id.button_app).setOnClickListener {
            postEvent(GlobalEvent("send by TestFragment"))
        }

        view.findViewById<Button>(R.id.button_fragment).setOnClickListener {
            postEvent(this@TestFragment, FragmentEvent("send by TestFragment"))
        }

        view.findViewById<Button>(R.id.button_activity).setOnClickListener {
            postEvent(requireActivity(), ActivityEvent("send by  TestFragment"))
        }

        view.findViewById<Button>(R.id.button_component).setOnClickListener {
            test.sendEvent(this)
        }

        //监听全局事件
        observeEvent<GlobalEvent> {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent 1:${it.name}")
        }
        observeEvent<GlobalEvent>(isSticky = true) {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent 2:${it.name}")
        }

        observeEvent<GlobalEvent>(dispatcher = Dispatchers.IO) {
            Log.d(MainActivity.TAG, "TestFragment received GlobalEvent 3:${it.name}")
        }


        //监听Activity事件
        observeEvent<ActivityEvent>(scope = requireActivity(), isSticky = true) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 2:${it.name}")
        }

        observeEvent<ActivityEvent>(
            scope = requireActivity(),
            minActiveState = Lifecycle.State.STARTED
        ) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 3:${it.name}")
        }

        observeEvent<ActivityEvent>(scope = requireActivity(), Dispatchers.IO) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 4:${it.name}")
        }

        observeEvent<ActivityEvent>(
            scope = requireActivity(),
            Dispatchers.IO,
            Lifecycle.State.STARTED
        ) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 5:${it.name}")
        }

        //监听自己Scope事件
        observeEvent<FragmentEvent>(scope = this, Dispatchers.IO, Lifecycle.State.STARTED) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 1:${it.name}")
        }

        observeEvent<FragmentEvent>(scope = this, minActiveState = Lifecycle.State.STARTED) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 2:${it.name}")
        }

        observeEvent<FragmentEvent>(scope = this, Dispatchers.IO) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 3:${it.name}")
        }

        observeEvent<FragmentEvent>(scope = this, isSticky = true) {
            Log.d(MainActivity.TAG, "TestFragment received FragmentEvent 4:${it.name}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}