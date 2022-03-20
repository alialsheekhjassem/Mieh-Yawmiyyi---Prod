package com.magma.miyyiyawmiyyi.android.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.presentation.home.HomeActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var notificationManager: NotificationManager? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("TAG", "onMessageReceived: $remoteMessage")

        //background
        if (remoteMessage.notification != null) {
            handleNotification(remoteMessage)
        } else if (remoteMessage.data.isNotEmpty() && remoteMessage.data
                .containsKey("Type")
        ) {
            handleNotification(remoteMessage)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "onNewToken: $token")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    private fun handleNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        Log.d("TAG", "MMM handleNotification: getData " + remoteMessage.data)
        if (remoteMessage.notification != null) {
            Log.d(
                "TAG",
                "MMM handleNotification: getNotification " + remoteMessage.notification?.title
            )
            Log.d(
                "TAG",
                "MMM handleNotification: getNotification " + remoteMessage.notification?.body
            )
        }
        if (remoteMessage.data.isNotEmpty() && remoteMessage.data.containsKey("Type")
        ) {
            intent.putExtra("Type", remoteMessage.data["Type"])
            intent.putExtra("userId", remoteMessage.data["userId"])
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel()
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, packageName)
        var title: String? = null
        var body: String? = null
        if (remoteMessage.notification != null) {
            title = remoteMessage.notification?.title
            body = remoteMessage.notification?.body
        }
        notificationBuilder
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setColor(resources.getColor(R.color.colorPrimary, theme))
            .setGroup(NOTIFICATION_GROUP)
            .setGroupSummary(IS_GROUP_SAMMERY)
            .setSound(defaultSoundUri)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(pendingIntent)
        IS_GROUP_SAMMERY = false
        val notificationManager = NotificationManagerCompat.from(this)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(++NOTIFICATION_ID, notificationBuilder.build())
        //notificationManager.notify(++NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)
            val description: String = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(packageName, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private var NOTIFICATION_ID = 0
        private const val NOTIFICATION_GROUP = "Abi_Kirshak_GROUP"
        private var IS_GROUP_SAMMERY = true
    }

    init {
        Log.d("TAG", "MyFirebaseMessagingService: ")
    }
}
