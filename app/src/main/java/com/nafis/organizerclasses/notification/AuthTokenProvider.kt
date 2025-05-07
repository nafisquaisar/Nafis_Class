package com.nafis.organizerclasses.notification

import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.base.Charsets
import com.nafis.organizerclasses.BuildConfig
import java.io.ByteArrayInputStream
import java.sql.DriverManager.println


object AuthTokenProvider {
    private const val FIREBASE_MESSAGING_SCOPE = BuildConfig.FIREBASE_MESSESING_SCOPE

//    var jsonString =BuildConfig.SERVICE_ACCOUNT_JSON

    var jsonString = ((("{"
            + "\"type\":\"service_account\","
            + "\"project_id\":\"nafis-coaching-center\","
            + "\"private_key_id\":\"" + BuildConfig.NOTIFICATION_ID) + "\","
            + "\"private_key\":\"" + BuildConfig.NOTIFICATION_PRIVATE_KEY) + "\","
            + "\"client_email\":\"firebase-adminsdk-r4fes@nafis-coaching-center.iam.gserviceaccount.com\","
            + "\"client_id\":\"100122120935846233257\","
            + "\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\","
            + "\"token_uri\":\"https://oauth2.googleapis.com/token\","
            + "\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\","
            + "\"client_x509_cert_url\":\"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-r4fes%40nafis-coaching-center.iam.gserviceaccount.com\","
            + "\"universe_domain\":\"googleapis.com\""
            + "}")

    fun getAccessToken(): String? {
        return try {
            val byteArray = jsonString.toByteArray(Charsets.UTF_8)
            ByteArrayInputStream(byteArray).use { stream ->
                val googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(listOf(FIREBASE_MESSAGING_SCOPE))

                googleCredentials.refreshIfExpired() // Ensure token is refreshed
                googleCredentials.refresh() // Manually refresh the token
                val token = googleCredentials.accessToken?.tokenValue
                println("Google Credentials: $googleCredentials")
                println("Access Token Object: ${googleCredentials.accessToken}")

                if (token.isNullOrEmpty()) {
                    throw Exception("Access token is empty")
                }

                println("Access Token: $token")
                token
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null instead of error string
        }
    }



}
