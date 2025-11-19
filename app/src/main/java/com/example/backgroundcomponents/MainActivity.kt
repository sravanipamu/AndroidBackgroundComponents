package com.example.backgroundcomponents

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.backgroundcomponents.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    private var boundService: MyBoundService? = null
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        // 1. Thread
        activityMainBinding.btnThread.setOnClickListener {
            Thread {
                for (i in 1..3) {
                    Log.d("ThreadExample", "Running thread: $i")
                    Thread.sleep(5000)
                }
            }.start()
        }

        // 2. Service
        activityMainBinding.btnService.setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }

        // 3. IntentService
        activityMainBinding.btnIntentService.setOnClickListener {
            val intent = Intent(this, MyIntentService::class.java)
            startService(intent)

        }

        // 4. JobIntentService
        activityMainBinding.btnJobIntentService.setOnClickListener {
            val intent = Intent(this, MyJobIntentService::class.java)
            MyJobIntentService.enqueueWork(this, intent)

        }

        // 5. Foreground Service
        activityMainBinding.btnForegroundService.setOnClickListener {
            val intent = Intent(this, MyForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(intent)
            else startService(intent)
        }

        // 6. Bound Service
        activityMainBinding.btnBoundService.setOnClickListener {
            if (isBound) {
                boundService?.startBackgroundTask("Upload Task") { result ->
                    runOnUiThread {
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 7. AsyncTask
        activityMainBinding.btnAsyncTask.setOnClickListener {
            MyAsyncTask().execute()
        }

        // 8. Handler
        activityMainBinding.btnHandler.setOnClickListener {
            val handlerThread = HandlerThread("DemoHandlerThread")
            handlerThread.start()

            val handler = Handler(handlerThread.looper) // Keeps a thread alive to handle multiple tasks sequentially
            handler.post {
                for (i in 1..3) {
                    Log.d("HandlerDemo", "Iteration $i running on ${Thread.currentThread().name}")
                    Thread.sleep(2000) // ⏸ Sleep for 2 seconds between iterations
                }
                Log.d("HandlerDemo", "All iterations finished")
            }
        }

        // 9. WorkManager
        activityMainBinding.btnWorkManager.setOnClickListener {
            val workRequest = PeriodicWorkRequestBuilder<UploadWorker>(15, TimeUnit.MINUTES).build()
           // WorkManager.getInstance(this).enqueue(workRequest)
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "SyncWork",
                ExistingPeriodicWorkPolicy.KEEP, // or REPLACE
                workRequest
            )
        }

        // 10. JobScheduler
        activityMainBinding.btnJobScheduler.setOnClickListener {
            val component = ComponentName(this, MyJobService::class.java)
            val jobInfo = JobInfo.Builder(1, component)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
            val scheduler = getSystemService(JobScheduler::class.java)
            scheduler.schedule(jobInfo)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyBoundService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBoundService.LocalBinder
            boundService = binder.getService()
            isBound = true
            Log.d("BoundService", "Connected to Bound Service")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            boundService = null
        }
    }
}