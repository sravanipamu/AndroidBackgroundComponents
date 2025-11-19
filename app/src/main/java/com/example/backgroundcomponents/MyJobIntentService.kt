package com.example.backgroundcomponents

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        for (i in 1..3) {
            Log.d("MyJobIntentService", "Processing task $i")
            Thread.sleep(1000)
        }
    }

    companion object {
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MyJobIntentService::class.java, 1000, work)
        }
    }
}