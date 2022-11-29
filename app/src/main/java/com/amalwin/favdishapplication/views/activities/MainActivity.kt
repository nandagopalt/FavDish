package com.amalwin.favdishapplication.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.databinding.ActivityMainBinding
import com.amalwin.favdishapplication.model.notification.NotifyWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        //val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes,
                R.id.navigation_favorite_dishes,
                R.id.navigation_random_dishes
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        startWork()
    }

    fun hideBottomNavView() {
        binding.navView.clearAnimation()
        binding.navView.animate()
            .translationY(binding.navView.height.toFloat()).setDuration(300)
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0F).setDuration(300)
        binding.navView.visibility = View.VISIBLE
    }

    private fun createConstraints() =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

    private fun createInputData() = Data.Builder().putString("Input Key", "Input Value").build()

    private fun createPeriodicWorkRequest() =
        PeriodicWorkRequestBuilder<NotifyWorker>(15L, TimeUnit.MINUTES).setConstraints(
            createConstraints()
        ).setInputData(createInputData()).build()

    private fun startWork() {
        WorkManager.getInstance(MainActivity@ this)
            .enqueueUniquePeriodicWork(
                "Periodic Notification",
                ExistingPeriodicWorkPolicy.KEEP,
                createPeriodicWorkRequest()
            )

        WorkManager.getInstance(MainActivity@ this)
            .getWorkInfoByIdLiveData(createPeriodicWorkRequest().id).observe(MainActivity@ this) {
                it?.let { workInfo ->
                    when (workInfo.state) {
                        WorkInfo.State.ENQUEUED -> NotifyWorker.logger("Task Enqueued !")
                        WorkInfo.State.RUNNING -> NotifyWorker.logger("Task Running !")
                        WorkInfo.State.SUCCEEDED -> NotifyWorker.logger("Task Succeeded !")
                        WorkInfo.State.BLOCKED -> NotifyWorker.logger("Task Blocked !")
                        WorkInfo.State.CANCELLED -> NotifyWorker.logger("Task Cancelled !")
                        WorkInfo.State.FAILED -> NotifyWorker.logger("Task Failed !")
                    }
                    val outputData = workInfo.outputData.getString("Output_Key")
                    Log.i("Notify Worker", "$outputData")
                }
            }
    }

    /*PeriodicWorkRequest.Builder(NotifyWorker::class.java, 15, TimeUnit.MINUTES)
       .setConstraints(createConstraints())
       .setInputData(createInputData())*/


    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || NavigationUI.navigateUp(
            navController,
            null
        ) || navController.navigateUp()
    }
}