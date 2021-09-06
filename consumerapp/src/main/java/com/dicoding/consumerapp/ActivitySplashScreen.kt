package com.dicoding.consumerapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class ActivitySplashScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@ActivitySplashScreen, MainActivity::class.java))
            finish()
        },3000)
    }
}