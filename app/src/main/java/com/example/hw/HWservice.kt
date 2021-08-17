package com.example.hw

import android.app.Service
import android.content.Intent
import android.os.IBinder

class HWservice : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("fReturn XXX the communication channel to the service.")
    }
}