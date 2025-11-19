package com.example.backgroundcomponents

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyIntentService : IntentService("MyIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        for (i in 1..3) {
            Log.d("MyIntentService", "Processing task $i")
            Thread.sleep(1000)
        }
    }

}