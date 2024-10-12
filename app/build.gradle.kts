plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.msdc.rentalwheels"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    namespace = "com.msdc.rentalwheels"
}

dependencies {
    val composeVersion = "1.5.4"
    val lifecycleVersion = "2.7.0"
    val roomVersion = "2.6.1"
    val hiltVersion = "2.48.1"
    val retrofitVersion = "2.9.0"
    val okhttpVersion = "4.12.0"
    val navVersion = "2.7.6"
    val coilVersion = "2.5.0"

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:28.4.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")

    // Google Play Services
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.libraries.places:places:3.5.0")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Material Design
    implementation("com.google.android.material:material:1.12.0")

    // Gson
    implementation("com.google.code.gson:gson:2.8.7")

    // OkHttp3
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // SweetAlert
    implementation("cn.pedant.sweetalert:library:1.3")

    // Okio
    implementation("com.squareup.okio:okio:2.9.0")

    // Flutterwave
    implementation("com.github.flutterwave.rave-android:rave_android:2.1.38")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-graphics:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material3:material3-android:1.3.0")
    implementation("androidx.compose.material:material:1.5.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("androidx.compose.runtime:runtime-android:1.7.3")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")

    // AppCompat and Core
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity:1.9.1")

    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.0-rc01")

    // Room
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-work:1.1.0")
    implementation("androidx.work:work-runtime-ktx:2.7.0")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    implementation("androidx.compose.runtime:runtime-livedata:1.3.3")
}

secrets {
    defaultPropertiesFileName = "gradle.properties"
}