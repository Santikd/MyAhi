package com.dicoding.myahi.data.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TaskExecutors {
    val ioExecutor: Executor = Executors.newSingleThreadExecutor()
    val networkExecutor: Executor = Executors.newFixedThreadPool(3)
    val uiExecutor: Executor = UIThreadExecutor()

    private class UIThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }
}
