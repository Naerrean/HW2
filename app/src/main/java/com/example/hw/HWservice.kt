package com.example.hw

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast



class HWservice : Service() {

    private val tag = "Rurri"
    private var flag = false
    private var counter = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        action("It's exist")
        Toast.makeText(applicationContext, "It's exist", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        action("It's start working")

        Thread {
            while (!flag){
                counter++
                action(counter.toString())
                Thread.sleep(1000)
            }
        }.start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        flag = true
        super.onDestroy()
        action("It's killed")
    }

    private fun action (message: String){
        Log.d(tag , message)
    }
}

