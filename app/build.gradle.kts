plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.9.22"
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.madfinal.launguageapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.madfinal.launguageapp"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Bottom Navigation
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")

// RxJava (оставлены только самые новые версии)
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

// Navigation
    val nav_version = "2.8.9"
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.3.0-beta01")

// Room
    implementation("androidx.room:room-runtime:2.3.0")
    annotationProcessor("androidx.room:room-compiler:2.3.0")

// Другие Android зависимости
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.webkit:webkit:1.4.0")
    implementation("androidx.core:core-ktx:1.9.0")

// Google Maps и Auth
    implementation("com.google.android.gms:play-services-maps:17.0.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

// Yandex Maps
    implementation("com.yandex.android:maps.mobile:4.4.0-full")

// Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

// Coil
    implementation("io.coil-kt:coil:2.5.0")

// Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.5.1")

// Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

// OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

// Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:2.3.1"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:gotrue-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")

// Ktor
    implementation("io.ktor:ktor-client-okhttp:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.8")

// Koin (оставлены только самые новые версии)
    implementation("io.insert-koin:koin-core:4.0.1")
    implementation("io.insert-koin:koin-android:4.0.1")
    implementation("io.insert-koin:koin-core-viewmodel:4.0.1")
    implementation("io.insert-koin:koin-core-coroutines:4.0.1")

// Прочие зависимости
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    implementation("com.google.zxing:core:3.4.1")

// Тесты
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")

    //image croper
    implementation("com.github.CanHub:Android-Image-Cropper:4.3.2")


    // tenserflow
    implementation("org.tensorflow:tensorflow-lite:2.9.0")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.3.1")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0")

    //Ar
    val ar_core_version = "1.31.0"
    val sceneform_version = "1.17.1"

    implementation("com.google.ar:core:$ar_core_version")
    implementation("com.google.ar.sceneform.ux:sceneform-ux:$sceneform_version")
    implementation("com.google.ar.sceneform:core:$sceneform_version")
    implementation("com.google.ar.sceneform:assets:$sceneform_version")


    // PDF Viewer

    implementation("io.github.afreakyelf:Pdf-Viewer:2.3.2")

}