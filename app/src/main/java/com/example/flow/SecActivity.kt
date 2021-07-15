package com.example.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.post.postDelayEvent
import com.biubiu.eventbus.post.postEvent
import com.example.flow.MainActivity.Companion.NORMAL_EVENT
import com.example.flow.MainActivity.Companion.TAG
import com.example.flow.databinding.ActivitySecBinding
import com.example.flow.event.CustomEvent

class SecActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = ActivitySecBinding.inflate(layoutInflater)
        setContentView(root.root)
        //粘性事件
        observeEvent<String>(MainActivity.STICKY) { value ->
            Log.d(TAG, "received :接收！ $value")
        }

        root.sendSimpleEvent.setOnClickListener {
            postEvent(NORMAL_EVENT, "Let's do it")
        }
        root.sendSimpleDelayEvent.setOnClickListener {
            postDelayEvent(NORMAL_EVENT, "Let's do it delay", 1000)
        }

        root.sendCustomEvent.setOnClickListener {
            postEvent(CustomEvent(name = "Hello SharedFlow now"))

        }

        root.sendCustomDelayEvent.setOnClickListener {
            postDelayEvent(CustomEvent(name = "Hello SharedFlow delay"), 1000)
        }

        root.send100Event.setOnClickListener {
            for (index in 1..100) {
                postEvent(MainActivity.CHECK_EVENT, index)
            }
        }
    }
}