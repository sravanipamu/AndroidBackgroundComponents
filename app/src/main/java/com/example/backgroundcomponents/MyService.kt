package com.example.backgroundcomponents

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            for (i in 1..3) {
                Log.d("MyService", "Running Service Task $i "+ " flag  = $flags startId = $startId")
                Thread.sleep(5000)
            }
            stopSelf()
        }.start()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?) = null
}