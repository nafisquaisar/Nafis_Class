package com.nafis.organizerclasses.notification

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object FCMNotificationSender {

     fun sendNotification(title: String, message: String, topic: String = "users") {
        GlobalScope.launch(Dispatchers.IO) {
            val token = AuthTokenProvider.getAccessToken()

            if (token.isNullOrEmpty()) {
                Log.e("FCM", "Failed to get access token!")
                return@launch
            }

            val json = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("topic", topic) // Change "token" to "topic"
                    put("notification", JSONObject().apply {
                        put("title", title)
                        put("body", message)
                    })
                })
            }



            val client = OkHttpClient()
            val requestBody =
                RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
            val request = Request.Builder()
                .url("https://fcm.googleapis.com/v1/projects/nafis-coaching-center/messages:send") // Update your project ID
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            Log.d("FCM", "Response Code: ${response.code}")
            Log.d("FCM", "Response Body: $responseBody")
            Log.d("FCM", "Token : $token")
            Log.d("FCM", "titke : $title")
            Log.d("FCM", "message : $message")
            Log.d("FCM", "topic : $topic")

            if (response.isSuccessful) {
                Log.d("FCM", "Notification sent successfully!")
            } else {
                Log.e("FCM", "Failed to send notification: $responseBody")
            }
        }
    }
}
