package com.example.backgroundcomponents

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(context: Context, params: WorkerParameters)  : Worker(context, params) {
    override fun doWork(): Result {
        for (i in 1..3) {
            Log.d("WorkManager", "Uploading file... $i")
            Thread.sleep(1000)
        }
        return Result.success()
    }
}