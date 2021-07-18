package com.example.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.biubiu.eventbus.observe.observeGlobalEvent
import com.biubiu.eventbus.post.postDelayEvent
import com.biubiu.eventbus.post.postEvent
import com.biubiu.eventbus.post.postGlobalDelayEvent
import com.biubiu.eventbus.post.postGlobalEvent
import com.example.flow.MainActivity.Companion.NORMAL_EVENT
import com.example.flow.MainActivity.Companion.TAG
import com.example.flow.databinding.ActivitySecBinding
import com.example.flow.event.ActivityEvent
import com.example.flow.event.GlobalEvent

class SecActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = ActivitySecBinding.inflate(layoutInflater)
        setContentView(root.root)
        //粘性事件
        observeGlobalEvent<String>(MainActivity.STICKY) { value ->
            Log.d(TAG, "received :接收！ $value")
        }

        root.sendSimpleEvent.setOnClickListener {
            postGlobalEvent(GlobalEvent("Sec Activity"))
        }
        root.sendSimpleDelayEvent.setOnClickListener {
            postGlobalDelayEvent(NORMAL_EVENT, "Let's do it delay", 1000)
        }

        root.sendCustomEvent.setOnClickListener {
            postEvent(ActivityEvent(name = "Hello SharedFlow now"))
        }

        root.sendCustomDelayEvent.setOnClickListener {
            postDelayEvent(ActivityEvent(name = "Hello SharedFlow delay"), 1000)
        }

        root.send100Event.setOnClickListener {
            for (index in 1..100) {
                postEvent(MainActivity.CHECK_EVENT, index)
            }
        }
    }
}