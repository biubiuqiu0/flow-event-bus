package com.example.flow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.post.postEvent
import com.example.flow.databinding.ActivityMainBinding
import com.example.flow.event.ActivityEvent
import com.example.flow.event.GlobalEvent
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "FlowEventBus"
        val STICKY = "sticky"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = ActivityMainBinding.inflate(layoutInflater)

        setContentView(root.root)

        observeGlobalEvents()
        observeActivityScopeEvents()

        supportFragmentManager.beginTransaction().replace(R.id.frame, TestFragment())
            .commitAllowingStateLoss()

        root.sendActivityEvent.setOnClickListener {
            postEvent(this, ActivityEvent("send by Activity"))
        }


        root.sendEvent.setOnClickListener {
            postEvent(GlobalEvent("send by Activity"))
        }

        root.openSec.setOnClickListener {
            startActivity(Intent(this, SecActivity::class.java))
        }

    }

    //跨页面
    private fun observeGlobalEvents() {
        //全局事件
        observeEvent<GlobalEvent> { value ->
            Log.d(TAG, "MainActivity received GlobalEvent  :${value.name}")
        }
    }


    //本页面
    private fun observeActivityScopeEvents() {
        //Activity 级别的 事件
        //自定义事件
        observeEvent<ActivityEvent>(this) {
            Log.d(TAG, "MainActivity received ActivityEvent: ${it.name}")
        }

//        //自定义事件 切换线程
        observeEvent<ActivityEvent>(Dispatchers.IO) {
            Log.d(TAG, "received ActivityEvent:${it.name} " + Thread.currentThread().name)
        }
//
//        //自定义事件 指定最小生命周期
        observeEvent<ActivityEvent>(minActiveState = Lifecycle.State.DESTROYED) {
            Log.d(TAG, "received ActivityEvent:${it.name}   >  DESTROYED")
        }

//        //自定义事件 切换线程 + 指定最小生命周期
//        observeEvent<ActivityEvent>(Dispatchers.IO, Lifecycle.State.DESTROYED) {
//            Log.d(
//                TAG,
//                "received ActivityEvent:${it.name} >  DESTROYED    " + Thread.currentThread().name
//            )
//        }

    }

}