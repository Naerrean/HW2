package com.example.hw


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import com.example.hw.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var counter: Int = 0
    private var flag: Boolean = false
    private var channelId: String = "101"
    private lateinit var mService: HWservice
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as HWservice.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    companion object {
        init {
            System.loadLibrary("hw")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel(channelId)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread {
            flag = true
            while (flag){
                Thread.sleep(1000)

                runOnUiThread {
                    binding.c.text = counter.toString()
                    binding.c.rotation *= -1
                    binding.Label.rotation *= -1
                    binding.Label.text = stringFromJNI(counter)
                }
                counter++
            }
        }.start()

        binding.startBtn.setOnClickListener {
            if (!mBound) {

                Intent(this, HWservice::class.java).also { intent ->
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                }
                startService(Intent(this, HWservice::class.java))
                binding.getfsBtn.isEnabled = true
                binding.gifImageView.isVisible = true
            }
            else
                toast(getString(R.string.mOlOn))
        }

        binding.stopBtn.setOnClickListener{
            if (mBound) {
                unbindService(connection)
                mBound = false
                stopService(Intent(this, HWservice::class.java))
                binding.getfsBtn.isEnabled = false
                binding.gifImageView.isVisible = false
            }
            else
                toast(getString(R.string.mOlOff))
        }

        binding.tik.setOnClickListener{
            if (binding.tik.text != getString(R.string.b2)) {
                binding.tik.text = getString(R.string.b2)
                binding.Label.text = getString(R.string.t2)
                binding.cl.setBackgroundColor(Color.CYAN)
            }
            else{
                binding.tik.text = getString(R.string.b1)
                binding.Label.text= getString(R.string.t1)
                binding.cl.setBackgroundColor(Color.LTGRAY)
            }

            setNotification(binding.tik.text as String, "${binding.Label.text as String}  $counter раз ))")
        }

        binding.getfsBtn.setOnClickListener {
            toast("Значение счетсчика службы : ${mService.getCounter}")
        }
    }

    override fun onDestroy() {
        flag = false
        super.onDestroy()
    }

    private fun createNotificationChannel(channelId:String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "My Channel"
            val channelDescription = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId,name,importance)
            channel.apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setNotification(titleN: String , textN: String){

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_hammer)
            .setContentTitle(titleN)
            .setContentText(textN)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(counter, builder.build())
        }
    }

    private external fun stringFromJNI(key :Int): String

    private fun Context.toast(message:String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}