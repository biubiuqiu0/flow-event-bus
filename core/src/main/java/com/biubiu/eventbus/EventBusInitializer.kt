package com.biubiu.eventbus

import android.app.Application
import com.biubiu.eventbus.util.ILogger

object EventBusInitializer {

    lateinit var application: Application

    var logger: ILogger? = null

    fun init(application: Application, logger: ILogger? = null) {
        EventBusInitializer.application = application
        this.logger = logger
    }

}