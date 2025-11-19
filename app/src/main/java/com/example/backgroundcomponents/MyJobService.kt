package com.example.backgroundcomponents

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class MyJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Thread {
            for (i in 1..3) {
                Log.d("JobScheduler", "Executing Job $i")
                Thread.sleep(1000)
            }
            jobFinished(params, false)
        }.start()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean = false
}