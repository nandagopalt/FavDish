package com.amalwin.favdishapplication.views.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.databinding.ActivityLaunchScreenBinding

class LaunchScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launchScreenBinding: ActivityLaunchScreenBinding =
            ActivityLaunchScreenBinding.inflate(layoutInflater)
        setContentView(launchScreenBinding.root)
        //launchScreenBinding.tvSplashTitle.text = "Hello World !"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val splashScreenTitleAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        launchScreenBinding.tvSplashTitle.animation = splashScreenTitleAnimation
        splashScreenTitleAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    startActivity(Intent(this@LaunchScreenActivity, MainActivity::class.java))
                    finish()
                }, 2000)
            }

            override fun onAnimationRepeat(p0: Animation?) {
                //
            }
        })
    }
}