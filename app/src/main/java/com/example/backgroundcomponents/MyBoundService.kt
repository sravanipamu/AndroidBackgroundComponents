package com.example.backgroundcomponents

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyBoundService : Service() {

    var binder = LocalBinder()
    inner class LocalBinder : Binder() {
        fun getService(): MyBoundService = this@MyBoundService
    }
    override fun onBind(intent: Intent?): IBinder? {
       return binder
    }
    // Function to start background work
    fun startBackgroundTask(taskName: String, callback: (String) -> Unit) {
        Thread {
            for (i in 1..5) {
                Thread.sleep(1000) // Simulate work
                Log.d("MyBoundService", "$taskName - Step $i")
            }
            callback("$taskName Completed")
        }.start()
    }
    fun getCurrentTime(): String =
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
}