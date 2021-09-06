package com.dicoding.androidfundamentalsubmission3.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dicoding.androidfundamentalsubmission3.MainActivity
import com.dicoding.androidfundamentalsubmission3.R
import java.util.*

class ServiceAlarm: BroadcastReceiver() {

    companion object {
        private const val CHAN_ID = "channel_69"
        private const val CHAN_NAME = "Reminder"
        private const val REQUEST_CODE = 69
        private const val NOTIF_ID = REQUEST_CODE
    }

    override fun onReceive(context: Context, intent: Intent) {
        val reminder = context.getString(R.string.reminder)
        val message = context.getString(R.string.reminder_message)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,0)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHAN_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.github)
                .setContentTitle(reminder)
                .setContentText(message)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(sound)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHAN_ID, CHAN_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHAN_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIF_ID, notification)
    }

    fun setRepeatReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ServiceAlarm::class.java)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
    }

    fun setAlarmReminder(contxt: Context): Boolean {
        val intent = Intent(contxt, ServiceAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(contxt, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE)
        return pendingIntent != null
    }

    fun cancelRepeatReminder(contxt: Context) {
        val alarmManager = contxt.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(contxt, ServiceAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(contxt, REQUEST_CODE, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }
}