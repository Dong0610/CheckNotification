package com.example.checknotification.base


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.checknotification.R
import java.util.Random


class CountdownService : Service() {
    private var isCountingDown = false
    private var count = 0
    private var timer: CountDownTimer? = null
    var customID:Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startCountdown()
        customID= ranidNotify

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startCountdown() {
//        Toast.makeText(applicationContext, "Start Service", Toast.LENGTH_LONG).show()
        if (!isCountingDown) {
            isCountingDown = true
            timer = object : CountDownTimer(3000, 1000) { // 10 seconds countdown
                override fun onTick(millisUntilFinished: Long) {
                }
                override fun onFinish() {

                    showNotification(customID,notification = "Run backGround Hello")
                    stopCountdown()
                }
            }.start()
        }
    }

    private fun stopCountdown() {
//        Toast.makeText(applicationContext, "Stop Service", Toast.LENGTH_LONG).show()
        timer?.cancel()
        timer = null
        isCountingDown = false
        count = 0
        stopSelf()
    }

    fun showNotification(
        customID:Int,
        title: String = "Notification", notification: Any, icon: Int = R.mipmap.ic_launcher
    ) {

        val lastIntent =HistoryManager.instance?.popFromHistory();

        if (lastIntent != null) {

            val pendingIntent =
                PendingIntent.getActivity(this, 0, lastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder =
                NotificationCompat.Builder(applicationContext, "channelId").setSmallIcon(icon)
                    .setContentTitle(title).setContentText(notification.toString()+"$customID")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(icon, "Detail", pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "channelId"
                val channelName = "Channel Name"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance)

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                builder.setChannelId(channelId)
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(123, builder.build())
        }
    }

}

class HistoryManager private constructor() {
    private val history: MutableList<Intent> = ArrayList()
    fun addToHistory(intent: Intent) {
        history.add(intent)
    }

    fun popFromHistory(): Intent? {
        return if (!history.isEmpty()) {
            history.removeAt(history.size - 1)
        } else null
    }

    companion object {
        var instance: HistoryManager? = null
            get() {
                if (field == null) {
                    field = HistoryManager()
                }
                return field
            }
            private set
    }
}


class ActivityData(private val className: String) {

    fun getClassName(): String {
        return className
    }
}

class NavigationHistoryManager private constructor() {
    private val history = mutableListOf<ActivityData>()

    companion object {
        private var instance: NavigationHistoryManager? = null

        fun getInstance(): NavigationHistoryManager {
            if (instance == null) {
                instance = NavigationHistoryManager()
            }
            return instance!!
        }
    }

    fun addToHistory(data: ActivityData) {
        history.add(data)
    }

    fun popFromHistory(): ActivityData? {
        if (history.isNotEmpty()) {
            return history.removeAt(history.size - 1)
        }
        return null
    }
}


private fun getClassByName(className: String): Class<*>? {
    try {
        return Class.forName(className)
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    }
    return null
}



class NotificationDeleteService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationId = intent?.getIntExtra("notificationId", -1)
        if (notificationId != -1) {
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.cancel(notificationId!!)
        }
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

val ranidNotify= Random().nextInt()

