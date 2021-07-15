package com.example.flow

import android.app.Application
import com.biubiu.eventbus.EventBusInitializer

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        EventBusInitializer.init(this)
    }
}