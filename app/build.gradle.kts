import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")

}
// Load properties from local.properties
val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

// Get values from local.properties
val notificationId = localProperties.getProperty("notificationid") ?: ""
val notificationPrivateKey = localProperties.getProperty("NOTIFICATION_PRIVATE_KEY") ?: ""
val emailPassword = localProperties.getProperty("emailpassword") ?: ""
val razorpayKey = localProperties.getProperty("razorpayKey") ?: ""
val firebasaeMessesingScope = localProperties.getProperty("firebasaeMessesingScope") ?: ""
//val serviceAccountJson = localProperties.getProperty("SERVICE_ACCOUNT_JSON")


android {
    namespace = "com.nafis.organizerclasses"
    compileSdk = 34

    packagingOptions {
        exclude ("META-INF/DEPENDENCIES")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.nafis.organizerclasses"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        var properties = Properties()
        properties.load(FileInputStream(rootProject.file("local.properties")))

        // Use the loaded values in BuildConfig
        buildConfigField("String", "NOTIFICATION_ID", "\"$notificationId\"")
        buildConfigField("String", "NOTIFICATION_PRIVATE_KEY", "\"$notificationPrivateKey\"")
        buildConfigField("String", "EMAIL_PASSWORD", "\"$emailPassword\"")
        buildConfigField("String", "RAYZORPAY_KEY", "\"$razorpayKey\"")
        buildConfigField("String", "FIREBASE_MESSESING_SCOPE", "\"$firebasaeMessesingScope\"")
//        buildConfigField("String", "SERVICE_ACCOUNT_JSON", "\"$serviceAccountJson\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt")
            )


        }
    }



    compileOptions {
        sourceCompatibility =JavaVersion.VERSION_17
        targetCompatibility =JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    implementation("com.google.firebase:firebase-messaging-ktx:24.1.0")
    implementation("com.google.firebase:firebase-messaging:24.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.20")
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    // pdf viewer dependency
    implementation("com.squareup.okhttp3:okhttp:4.12.0")



    implementation ("androidx.multidex:multidex:2.0.1") // If multidex is enabled
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.8.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")

    // lotti animation
    implementation ("com.airbnb.android:lottie:3.4.0")

    //dynamic links
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Add the dependencies for the Dynamic Links and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation ("com.google.firebase:firebase-dynamic-links")
    implementation ("com.google.firebase:firebase-analytics")

    implementation ("com.razorpay:checkout:1.6.40")
    implementation ("com.itextpdf:itext7-core:7.2.5") // Or the latest version


//    implementation ("com.twilio.sdk:twilio:8.31.0")



        // mail sender
        implementation ("com.sun.mail:android-mail:1.6.2")
        implementation ("com.sun.mail:android-activation:1.6.2")

    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0") // Google OAuth
    implementation("com.squareup.okhttp3:okhttp:4.9.3") // For making HTTP requests
    implementation("com.google.code.gson:gson:2.8.9") // JSON parsing



    // MPAndroidChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

}
