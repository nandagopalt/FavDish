package com.amalwin.favdishapplication.model.notification

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val inputData = inputData.getString("Input Key")
        Log.i("Notify Worker", "Input Data : $inputData")
        Log.i(
            "Notify Worker",
            "${generateDateFormat(System.currentTimeMillis())} doWork function is called..."
        )
        return Result.success(generateOutputData())
    }

    private fun generateOutputData(): Data =
        Data.Builder().putString("Output_Key", "Output Value").build()

    private fun generateDateFormat(timeInMillis: Long): String {
        val simpleDateFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return simpleDateFormatter.format(calendar.time)
    }

    companion object {
        fun logger(message: String) = Log.i("Notify Worker", message)
    }


}