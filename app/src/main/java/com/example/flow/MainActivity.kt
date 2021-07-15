package com.example.flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import com.example.flow.databinding.ActivityMainBinding
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.post.postStickyEvent
import com.example.flow.event.CustomEvent
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "FlowEventBus"
        val STICKY = "sticky"
        val NORMAL_EVENT = "normal_event"
        val CHECK_EVENT = "check_event"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = ActivityMainBinding.inflate(layoutInflater)

        setContentView(root.root)

        root.stickyBtn.setOnClickListener {
            postStickyEvent(STICKY, "☝ 粘性事件️")
        }

        root.openSec.setOnClickListener {
            startActivity(Intent(this, SecActivity::class.java))
        }
        //简单事件
        observeEvent<Int>(CHECK_EVENT) { value ->
            Log.d(TAG, "received :$value")
        }

        observeEvent<Int>(CHECK_EVENT, Lifecycle.State.DESTROYED, Dispatchers.IO) { value ->
            Log.d(TAG, "received  pause & io:$value")
        }
        //简单事件
        observeEvent<String>(NORMAL_EVENT) { value ->
            Log.d(TAG, "received :$value")
        }

        //指定最小生命周期
        observeEvent<String>(NORMAL_EVENT, Lifecycle.State.DESTROYED) { value ->
            Log.d(TAG, "received :$value > DESTROYED")
        }

        //切换线程接收 简单事件
        observeEvent<String>(NORMAL_EVENT, Dispatchers.IO) { value ->
            Log.d(TAG, "received :$value " + Thread.currentThread().name)
        }

        //切换线程 + 生命周期
        observeEvent<String>(NORMAL_EVENT, Lifecycle.State.DESTROYED, Dispatchers.IO) { value ->
            Log.d(TAG, "received :$value" + "  生命周期 > DESTROYED  " + Thread.currentThread().name)
        }

        //自定义事件
        observeEvent<CustomEvent> {
            Log.d(TAG, "received custom event:${it.name}")
        }

        //自定义事件 指定最小生命周期
        observeEvent<CustomEvent>(Lifecycle.State.DESTROYED) {
            Log.d(TAG, "received custom event:${it.name}   >  DESTROYED")
        }

        //自定义事件 切换线程
        observeEvent<CustomEvent>(Dispatchers.IO) {
            Log.d(TAG, "received custom event:${it.name}"+ Thread.currentThread().name)
        }

        //自定义事件 切换线程 + 指定最小生命周期
        observeEvent<CustomEvent>(Lifecycle.State.DESTROYED, Dispatchers.IO) {
            Log.d(TAG, "received custom event:${it.name} >  DESTROYED"+ Thread.currentThread().name)
        }

    }
}