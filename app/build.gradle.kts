import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    //ksp (такжже нужен для room, hilt и так далее)
    id("com.google.devtools.ksp")

    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.4.0" //версия плагина сериализации должна совпадать с версией Kotlin

    //Hilt
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.example.aproject"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.aproject"
        minSdk = 26
        targetSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Сoroutines
    // Основная библиотека корутин(Flow уже там)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    // Только для Android: поддержка главного потока (Main Dispatcher)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")

    //Navigation component
    implementation("androidx.navigation:navigation-compose:2.9.8")
    // Kotlin Serialization (для типобезопасной навигации)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.60")
    ksp("com.google.dagger:hilt-android-compiler:2.60")
    // Расширение для внедрения зависимостей в ViewModel
    implementation("androidx.hilt:hilt-lifecycle-viewmodel-compose:1.3.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    // Конвертер для преобразования JSON в объекты Kotlin (например, Gson)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    // Конвертер на основе kotlinx.serialization (рекомендуется)
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0")
    // Optional: OkHttp logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:5.4.0")

    //Material Icons
//    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

}