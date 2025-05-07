    package com.nafis.organizerclasses.notification

    import android.Manifest
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.media.AudioAttributes
    import android.media.RingtoneManager
    import android.os.Build
    import android.util.Log
    import androidx.core.app.ActivityCompat
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import com.google.firebase.messaging.FirebaseMessagingService
    import com.google.firebase.messaging.RemoteMessage
    import com.nafis.organizerclasses.DoubtActivity
    import com.nafis.organizerclasses.Main_dashboard
    import com.nafis.organizerclasses.R
    import com.nafis.organizerclasses.SplashActivity
    import kotlin.random.Random

    class MyFirebaseMessagingService : FirebaseMessagingService() {
        private val channelId = "organizer_classes"

        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            super.onMessageReceived(remoteMessage)

            val notification = remoteMessage.notification
            val notificationType = remoteMessage.data["notificationType"]
            Log.d("notification",notificationType!!)
            Log.d("FCM", "Notification: ${remoteMessage.notification?.title}, ${remoteMessage.notification?.body}")

            val intent = when (notificationType) {
                "doubt_resolved" -> Intent(this, SplashActivity::class.java).apply {
                    putExtra("doubtTitle", remoteMessage.data["doubtTitle"])
                    putExtra("userName", remoteMessage.data["userName"])
                    putExtra("doubt", notificationType)
                }
                else -> Intent(this, DoubtActivity::class.java) // Default screen
            }


            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (notification != null) {
                val title = notification.title ?: "New Notification"
                val desc = notification.body ?: "Click to view details"
                showNotification(title, desc, pendingIntent)
            }
        }

        private fun showNotification(title: String, message: String, pendingIntent: PendingIntent) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            // Create Notification Channel for Android 8+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId, "Organizer Classes", NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for Organizer Classes"
                    enableLights(true)
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 500, 200, 500)
                    setSound(soundUri, AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
                }

                val manager = getSystemService(NotificationManager::class.java)
                manager?.createNotificationChannel(channel)
            }

            // Build Notification
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setVibrate(longArrayOf(0, 500, 200, 500))

            val manager = NotificationManagerCompat.from(this)

            // Check for POST_NOTIFICATIONS permission (Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                    Log.e("FCM", "Notification permission not granted!")
                    return
                }
            }

            manager.notify(Random.nextInt(), builder.build())
        }
    }
