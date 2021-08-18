package com.example.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.post.postEvent
import com.biubiu.eventbus.util.clearStickyEvent
import com.biubiu.eventbus.util.getEventObserverCount
import com.biubiu.eventbus.util.removeStickyEvent
import com.example.flow.MainActivity.Companion.TAG
import com.example.flow.databinding.ActivitySecBinding
import com.example.flow.event.GlobalEvent

class SecActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = ActivitySecBinding.inflate(layoutInflater)
        setContentView(root.root)
        //粘性事件
        observeEvent<GlobalEvent>(isSticky = true) {
            Log.d(TAG, "SecActivity received ActivityEvent isSticky :${it.name}")
        }

        root.sendCustomEvent.setOnClickListener {
            postEvent(GlobalEvent(name = "Hello SharedFlow now"))
        }

        root.sendCustomDelayEvent.setOnClickListener {
            postEvent(GlobalEvent(name = "Hello SharedFlow delay"), 1000)
        }

        root.send100Event.setOnClickListener {
            for (index in 1..100) {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeStickyEvent(GlobalEvent::class.java)
    }
}