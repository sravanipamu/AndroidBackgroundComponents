package com.example.backgroundcomponents

import android.os.AsyncTask
import android.util.Log

class MyAsyncTask : AsyncTask<Void, Int, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        for (i in 1..3) {
            publishProgress(i)
            Thread.sleep(1000)
        }
        return "Task Finished"
    }
    override fun onProgressUpdate(vararg values: Int?) {
        Log.d("MyAsyncTask", "Progress: ${values[0]}")
    }

    override fun onPostExecute(result: String?) {
        Log.d("MyAsyncTask", "Result: $result")
    }
}