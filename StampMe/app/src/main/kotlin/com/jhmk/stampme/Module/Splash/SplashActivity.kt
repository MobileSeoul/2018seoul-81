package com.jhmk.stampme.Module.Splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jhmk.stampme.Main.MainActivity

class SplashActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity().javaClass)
        /*startActivity(intent)
        finish()*/
        try {
            Thread.sleep(2000)
            val intent = Intent(this, MainActivity().javaClass)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}